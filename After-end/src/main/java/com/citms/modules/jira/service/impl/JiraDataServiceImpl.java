package com.citms.modules.jira.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.citms.common.exception.RRException;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.developer.service.DeveloperHistoryService;
import com.citms.modules.jira.dao.ChangeLogMapper;
import com.citms.modules.jira.dao.IssueMapper;
import com.citms.modules.jira.dao.IssueSprintMapper;
import com.citms.modules.jira.entity.ChangeLogEntity;
import com.citms.modules.jira.entity.DictionaryEntity;
import com.citms.modules.jira.entity.IssueEntity;
import com.citms.modules.jira.entity.IssueSprintEntity;
import com.citms.modules.jira.enums.DictionaryEnum;
import com.citms.modules.jira.enums.WebhookEventEnum;
import com.citms.modules.jira.service.DictionaryService;
import com.citms.modules.jira.service.JiraDataService;
import com.citms.modules.jira.vo.issue.*;
import com.citms.modules.jira.vo.project.Project;
import com.citms.modules.jira.vo.project.ProjectVO;
import com.citms.modules.jira.vo.sprint.Sprint;
import com.citms.modules.jira.vo.sprint.SprintVO;
import com.citms.modules.project.dao.ProjectMapper;
import com.citms.modules.project.dao.SprintStatisticsMapper;
import com.citms.modules.project.entity.ProjectEntity;
import com.citms.modules.project.entity.SprintStatisticsEntity;
import com.citms.modules.project.service.ProjectService;
import com.citms.modules.sprintproject.dao.SprintMapper;
import com.citms.modules.sprintproject.dao.SprintProjectMapper;
import com.citms.modules.sprintproject.entity.SprintEntity;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.tester.service.TesterHistoryService;
import com.citms.modules.user.dao.UserMapper;
import com.citms.modules.user.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 */
@Service
public class JiraDataServiceImpl implements JiraDataService {

    @Autowired
    private UserMapper              userMapper;
    @Autowired
    private DictionaryService       dictionaryService;
    @Autowired
    private ProjectMapper           projectMapper;
    @Autowired
    private IssueMapper             issueMapper;
    @Autowired
    private SprintMapper            sprintMapper;
    @Autowired
    private ChangeLogMapper         changeLogMapper;
    @Autowired
    private IssueSprintMapper       issueSprintMapper;
    @Autowired
    private ProjectService          projectService;
    @Autowired
    private SprintProjectMapper     sprintProjectMapper;
    @Autowired
    private SprintStatisticsMapper  sprintStatisticsMapper;
    @Autowired
    private DeveloperHistoryService developerHistoryService;
    @Autowired
    private TesterHistoryService    testerHistoryService;
    @Autowired
    private SprintProjectService    sprintProjectService;

    /**
     * 将jira推送过来的数据存储到数据库
     * @param json
     */
    @Override
    public void saveJiraData(String json) throws InvocationTargetException, IllegalAccessException {
//        json = "{\t\"timestamp\": 1573181761924,\t\"webhookEvent\": \"sprint_started\",\t\"sprint\": {\t\t\"id\": 26,\t\t\"self\": \"http://192.168.0.120:8080/rest/agile/1.0/sprint/26\",\t\t\"state\": \"active\",\t\t\"name\": \"测试新项目面板 Sprint 1\",\t\t\"startDate\": \"2019-11-08T10:55:01.921+08:00\",\t\t\"endDate\": \"2019-11-22T10:55:00.000+08:00\",\t\t\"originBoardId\": 15,\t\t\"goal\": \"完成这个故事\"\t}}";
        if(StringUtils.isEmpty(json)){
            throw new RRException("json为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        String result = jsonObject.getString("webhookEvent");

        if(result.equals(WebhookEventEnum.ISSUE_CREATED.getName())
                || result.equals(WebhookEventEnum.ISSUE_UPDATED.getName())){
            convertIssue(json);
        }else if(result.equals(WebhookEventEnum.ISSUE_DELETED.getName())){
            deleteIssue(json);
        }else if(result.equals(WebhookEventEnum.PROJECT_CREATED.getName())
                 || result.equals(WebhookEventEnum.PROJECT_UPDATED.getName())){
            convertProject(json);
        }

        //迭代信息
        if(result.equals(WebhookEventEnum.SPRINT_DELETED.getName())){
            deleteSprint(json);
        }else if (result.contains(WebhookEventEnum.SPRINT_LABEL.getName())){
            convertSprint(json);
        }
    }

    private void convertIssue(String json)
            throws InvocationTargetException, IllegalAccessException {
        IssueVO issueVO = JSONObject.parseObject(json, IssueVO.class);
        if(issueVO == null){
            return;
        }

        saveUserInfo(issueVO.getUser());
        saveIssue(issueVO);
        saveChangeLog(issueVO);
    }

    /**
     * 删除问题
     * @param json
     */
    private void deleteIssue(String json){
        IssueVO issueVO = JSONObject.parseObject(json, IssueVO.class);
        if(issueVO == null) {
            return;
        }

        Issue issue = issueVO.getIssue();
        if(issue == null){
            throw new RRException("IssueVO信息为空");
        }

        //删除问题
        IssueEntity issueCon = new IssueEntity();
        issueCon.setId(Long.valueOf(issue.getId()));
        Wrapper<IssueEntity> ew = Wrappers.query(issueCon);
        issueMapper.delete(ew);

        //删除迭代问题关联信息
        IssueSprintEntity issueSprintCon = new IssueSprintEntity();
        issueSprintCon.setIssueId(Long.valueOf(issue.getId()));
        Wrapper<IssueSprintEntity> issueEw = Wrappers.query(issueSprintCon);
        issueSprintMapper.delete(issueEw);
    }

    /**
     * 保存用户信息
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private UserEntity saveUserInfo(User user)
            throws InvocationTargetException, IllegalAccessException {
        if(user == null){
            return null;
        }

        UserEntity userResult =  userMapper.queryUserByName(user.getName());
        if(userResult != null){
            user.setId(userResult.getId());
            return userResult;
        }

        //不存在，则存入数据库
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setDisplayName(user.getDisplayName());
        userEntity.setKeyValue(user.getKey());
        userEntity.setEmailAddress(user.getEmailAddress());

        Integer key = findKeyByCodeAndValue(DictionaryEnum.USER_ACTIVE.getName(), String.valueOf(user.isActive()));
        userEntity.setActive(key);
        userMapper.insert(userEntity);
        user.setId(userEntity.getId());
        return userEntity;
    }

    private Integer findKeyByCodeAndValue(String name, String value){
        Integer key = 0;
        if(StringUtils.isEmpty(value)){
            return key;
        }
        DictionaryEntity dictionaryEntity = dictionaryService.getDictionaryByCodeAndValue(name, value);
        if(dictionaryEntity != null && StringUtils.isNotEmpty(dictionaryEntity.getDictKey())){
            key = Integer.valueOf(dictionaryEntity.getDictKey());
        }
        return key;
    }

    private void saveIssue(IssueVO issueVO)
            throws InvocationTargetException, IllegalAccessException {
        Issue issue = issueVO.getIssue();
        if(issue == null){
            throw new RRException("IssueVO信息为空");
        }

        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setId(Long.valueOf(issue.getId()));
        issueEntity.setKeyValue(issue.getKey());

        Fields fields = issue.getFields();
        if(fields == null){
            throw new RRException("fields信息为空");
        }
        if(fields.getIssuetype() != null){
            Integer key = findKeyByCodeAndValue(DictionaryEnum.ISSUE_TYPE.getName(), fields.getIssuetype().getName());
            issueEntity.setIssuetype(key);
        }
        if(!CollectionUtils.isEmpty(fields.getComponents())){
            List<String> componentsNameList = fields.getComponents().stream().map(Components::getName).collect(toList());
            issueEntity.setComponents(StringUtils.join(componentsNameList, ","));
        }
        issueEntity.setTimespent(fields.getTimespent());
        issueEntity.setTimeoriginalestimate(fields.getTimeoriginalestimate());
        issueEntity.setDescription(subStringText(fields.getDescription()));
        ProjectEntity projectEntity = saveProjectInfo(issueVO);
        if(projectEntity != null){
            issueEntity.setProjectId(projectEntity.getId());
        }

        issueEntity.setAggregatetimespent(fields.getAggregatetimespent());

        Integer resolutionKey = null;
        if(fields.getResolution() != null){
            resolutionKey = findKeyByCodeAndValue(DictionaryEnum.ISSUE_RESOLUTION.getName(), fields.getResolution().getName());
        }else {
            resolutionKey = findKeyByCodeAndValue(DictionaryEnum.ISSUE_RESOLUTION.getName(), JiraConstants.RESOLUTION_UNSOLVED);
        }
        issueEntity.setResolution(resolutionKey);

        //缺陷来源
        if(fields.getCustomfield_10203() != null){
            Integer customfield10203Key = findKeyByCodeAndValue(DictionaryEnum.ISSUE_SOURCE.getName(), fields.getCustomfield_10203().getValue());
            issueEntity.setCustomfield10203(customfield10203Key);
        }

        issueEntity.setCustomfield10105(fields.getCustomfield_10105());
        issueEntity.setCustomfield10204(convertDateFormat(fields.getCustomfield_10204(),"yyyy-MM-dd"));
        issueEntity.setCustomfield10106(fields.getCustomfield_10106());
        //缺陷标记
        if(!CollectionUtils.isEmpty(fields.getCustomfield_10205())){
            List<String> defectList = fields.getCustomfield_10205().stream().map(Customfield::getValue).collect(toList());
            issueEntity.setCustomfield10205(StringUtils.join(defectList, ","));
        }
        if(!CollectionUtils.isEmpty(fields.getAttachment())){
            List<String> contentList = fields.getAttachment().stream().map(Attachment::getContent).collect(toList());
            issueEntity.setAttachment(subStringText(StringUtils.join(contentList, ",")));
        }
        issueEntity.setAggregatetimeestimate(fields.getAggregatetimeestimate());
        if(StringUtils.isNotEmpty(fields.getResolutiondate())){
            issueEntity.setResolutiondate(convertDate(fields.getResolutiondate()));
        }
        issueEntity.setWorkratio(fields.getWorkratio());
        issueEntity.setSummary(subStringText(fields.getSummary()));
        if(StringUtils.isNotEmpty(fields.getLastViewed())){
            issueEntity.setLastViewed(convertDate(fields.getLastViewed()));
        }
        if(fields.getWatches() != null){
            issueEntity.setWatches(String.valueOf(fields.getWatches().getIsWatching()));
        }

        UserEntity creator = saveUserInfo(fields.getCreator());
        if(creator != null){
            issueEntity.setCreatorId(creator.getId());
        }

        if(StringUtils.isNotEmpty(fields.getCreated())){
            issueEntity.setCreated(convertDate(fields.getCreated()));
        }

        UserEntity reporter = saveUserInfo(fields.getReporter());
        if(reporter != null){
            issueEntity.setReporterId(reporter.getId());
        }

        issueEntity.setCustomfield10000(subStringText(fields.getCustomfield_10000()));
        if(fields.getAggregateprogress() != null){
            issueEntity.setAggregateprogress(fields.getAggregateprogress().getProgress());
        }
        if(fields.getPriority() != null){
            Integer priorityKey = findKeyByCodeAndValue(DictionaryEnum.ISSUE_PRIORITY.getName(), fields.getPriority().getName());
            issueEntity.setPriority(priorityKey);
        }

        issueEntity.setCustomfield10100(subStringText(fields.getCustomfield_10100()));
        if(fields.getCustomfield_10201() != null){
            Integer customfield10201Key = findKeyByCodeAndValue(DictionaryEnum.ISSUE_DEFECT.getName(), fields.getCustomfield_10201().getValue());
            issueEntity.setCustomfield10201(customfield10201Key);
        }
        if(!CollectionUtils.isEmpty(fields.getLabels())){
            issueEntity.setLabels(StringUtils.join(fields.getLabels(), ","));
        }
        issueEntity.setTimeestimate(fields.getTimeestimate());
        issueEntity.setAggregatetimeoriginalestimate(fields.getAggregatetimeoriginalestimate());
        if(StringUtils.isNotEmpty(fields.getDuedate())){
            issueEntity.setDuedate(convertDateFormat(fields.getDuedate(),"yyyy-MM-dd"));
        }
        if(fields.getProgress() != null){
            issueEntity.setProgress(String.valueOf(fields.getProgress().getProgress()));
        }
        if(fields.getComment() != null && !CollectionUtils.isEmpty(fields.getComment().getComments())){
            issueEntity.setComment(subStringText(StringUtils.join(fields.getComment().getComments(), ",")));
        }
        if(!CollectionUtils.isEmpty(fields.getIssuelinks())){
            issueEntity.setIssuelinks(subStringText(StringUtils.join(fields.getIssuelinks(), ",")));
        }

        if(fields.getVotes() != null){
            issueEntity.setVotes(fields.getVotes().getVotes());
        }
        if(fields.getWorklog() != null && !CollectionUtils.isEmpty(fields.getWorklog().getWorklogs())){
            issueEntity.setWorklog(subStringText(StringUtils.join(fields.getWorklog().getWorklogs(), ",")));
        }

        //经办人
        UserEntity userAssignee = saveUserInfo(fields.getAssignee());
        if(userAssignee != null){
            issueEntity.setAssigneeId(userAssignee.getId());
        }
        if(StringUtils.isNotEmpty(fields.getUpdated())){
            issueEntity.setUpdated(convertDate(fields.getUpdated()));
        }

        if(fields.getStatus() != null){
            Integer statusKey = findKeyByCodeAndValue(DictionaryEnum.ISSUE_STATUS.getName(), fields.getStatus().getName());
            issueEntity.setStatus(statusKey);
            if(JiraConstants.STATUS_TESTED.equals(fields.getStatus().getName())){
                issueEntity.setTestedDate(new Date());
            }

            if(JiraConstants.STATUS_CLOSE.equals(fields.getStatus().getName())){
                issueEntity.setCloseDate(new Date());
                if(issueVO.getUser() != null){
                    issueEntity.setCloseUserId(issueVO.getUser().getId());
                }
            }
        }

        //存储迭代信息和迭代关联信息
        saveIssueSprint(issueVO, issueEntity);

        IssueEntity issueResult = issueMapper.queryIssueEntityById(Long.valueOf(issue.getId()));
        if(issueResult != null){
            IssueEntity issueCon = new IssueEntity();
            issueCon.setId(Long.valueOf(issue.getId()));
            Wrapper<IssueEntity> ew = Wrappers.query(issueCon);
            issueMapper.update(issueEntity, ew);
        }else{
            issueMapper.insert(issueEntity);
        }


    }

    private ProjectEntity saveProjectInfo(IssueVO issueVO){
        if(issueVO == null || issueVO.getIssue()== null || issueVO.getIssue().getFields() == null
                || issueVO.getIssue().getFields().getProject() == null){
            return null;
        }

        Project project = issueVO.getIssue().getFields().getProject();
        ProjectEntity projectResult =  projectMapper.queryProjectEntityById(Long.valueOf(project.getId()));
        if(projectResult != null){
            return projectResult;
        }

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(Long.valueOf(project.getId()));
        projectEntity.setKeyValue(project.getKey());
        projectEntity.setName(project.getName());
        projectEntity.setProjectTypeKey(project.getProjectTypeKey());
        projectMapper.insert(projectEntity);
        return projectEntity;
    }

    /**
     * 存储迭代信息和迭代关联信息
     * @param issueVO
     * @param issueEntity
     */
    private void saveIssueSprint(IssueVO issueVO, IssueEntity issueEntity){

        if (issueEntity.getId() != null) {

            // 删除该问题所有迭代关联数据，重新关联
            issueSprintMapper.deleteByIssueId(issueEntity.getId());
        }

        if(issueVO == null || issueVO.getIssue()== null || issueVO.getIssue().getFields() == null
           || StringUtils.isEmpty(issueVO.getIssue().getFields().getCustomfield_10104())){
            return;
        }

       String customfield10104 = issueVO.getIssue().getFields().getCustomfield_10104();
        //sprintId
        String[] sprints = customfield10104.split("\",\"");
        if(sprints == null){
            return;
        }

        for(int i=0;i<sprints.length;i++){
            //存储迭代信息
            saveSprintByIssue(sprints[i],issueEntity);

            //存储IssueSprint关联信息表
            String sprintId = sprints[i].substring(sprints[i].indexOf("id=")+3,sprints[i].indexOf(",rapidViewId"));
            IssueSprintEntity issueSprintEntity = new IssueSprintEntity();
            issueSprintEntity.setIssueId(issueEntity.getId());
            issueSprintEntity.setSprintId(Long.valueOf(sprintId));
            //问题是否新增，是：第一个迭代信息   否：除了第一个迭代信息
            issueSprintEntity.setIsSprintAdd(i==0 ? JiraConstants.IS_ADD_YES : JiraConstants.IS_ADD_NO);

            IssueSprintEntity issueSprintResult = issueSprintMapper.queryIssueSprintById(issueEntity.getId(), Long.valueOf(sprintId));
            if(issueSprintResult == null){
                issueSprintMapper.insert(issueSprintEntity);
            }else{
                IssueSprintEntity issueSprintCon = new IssueSprintEntity();
                issueSprintCon.setIssueId(issueEntity.getId());
                issueSprintCon.setSprintId(Long.valueOf(sprintId));
                Wrapper<IssueSprintEntity> ew = Wrappers.query(issueSprintCon);
                issueSprintMapper.update(issueSprintEntity, ew);
            }
        }

        //问题是否新增：否 = 关联的迭代数量>1   是=关联的迭代数量==1
//        if(sprints.length > 1){
//            issueEntity.setIsAdd(JiraConstants.IS_ADD_NO);
//        }else{
//            issueEntity.setIsAdd(JiraConstants.IS_ADD_YES);
//        }
    }

    /**
     * 存储迭代信息
     * @param sprintStr
     * @param issueEntity
     * @return
     */
    private SprintEntity saveSprintByIssue(String sprintStr, IssueEntity issueEntity){
        if(StringUtils.isEmpty(sprintStr)){
            return null;
        }

        SprintEntity sprintEntity = new SprintEntity();
        String sprintId = sprintStr.substring(sprintStr.indexOf("id=")+3,sprintStr.indexOf(",rapidViewId"));
        if(StringUtils.isNotEmpty(sprintId)){
            sprintEntity.setId(Long.valueOf(sprintId));
        }

        //迭代状态
        String state = sprintStr.substring(sprintStr.indexOf("state=")+6,sprintStr.indexOf(",name"));
        if(StringUtils.isNotEmpty(state) && !state.equals("<null>")){
            sprintEntity.setState(findKeyByCodeAndValue(DictionaryEnum.SPRINT_STATE.getName(), state));
        }

        //迭代名称
        String name = sprintStr.substring(sprintStr.indexOf("name=")+5,sprintStr.indexOf(",startDate"));
        sprintEntity.setName(name);

        //迭代开始日期
        String startDate = sprintStr.substring(sprintStr.indexOf("startDate=")+10,sprintStr.indexOf(",endDate"));
        if(StringUtils.isNotEmpty(startDate) && !startDate.equals("<null>")){
            sprintEntity.setStartDate(convertDateMore(startDate));
        }

        //迭代结束日期
        String endDate = sprintStr.substring(sprintStr.indexOf("endDate=")+8,sprintStr.indexOf(",completeDate"));
        if(StringUtils.isNotEmpty(endDate) && !endDate.equals("<null>")){
            sprintEntity.setEndDate(convertDateMore(endDate));
        }

        SprintEntity checkSprint = sprintMapper.querySprintById(sprintEntity.getId());
        if(checkSprint != null){
            SprintEntity sprintCon = new SprintEntity();
            sprintCon.setId(sprintEntity.getId());
            Wrapper<SprintEntity> ew = Wrappers.query(sprintCon);
            sprintMapper.update(sprintEntity, ew);
        }else{
            sprintMapper.insert(sprintEntity);
        }


        new Thread(() -> {
            try {
//                if(JiraConstants.SPRINT_STATE_CLOSED.equalsIgnoreCase(state) && StringUtils.isNotEmpty(sprintId)){
//                    //保存迭代统计信息
//                    projectService.saveSprintStatistics(Long.valueOf(sprintId));
//                }
                if(JiraConstants.SPRINT_STATE_ACTIVE.equalsIgnoreCase(state) && StringUtils.isNotEmpty(sprintId)){
                    //更新开发统计历史信息，更新下个迭代信息
                    developerHistoryService.updateNextSprintId(Long.valueOf(sprintId));
                    //更新测试统计历史信息，更新下个迭代信息
                    testerHistoryService.updateNextSprintId(Long.valueOf(sprintId));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        return sprintEntity;
    }

    private void saveChangeLog(IssueVO issueVO){
        if(issueVO.getChangelog() == null || StringUtils.isEmpty(issueVO.getChangelog().getId())
           || CollectionUtils.isEmpty(issueVO.getChangelog().getItems())){
            return;
        }

        List<ChangeLogEntity> changeLogList = changeLogMapper.queryChangeLogEntityById(Long.valueOf(issueVO.getChangelog().getId()));
        if(!CollectionUtils.isEmpty(changeLogList)){
            return;
        }

        List<ChangeLogItem> itemsList = issueVO.getChangelog().getItems();
        for(ChangeLogItem changeLogItem : itemsList){
            ChangeLogEntity changeLogEntity = new ChangeLogEntity();
            changeLogEntity.setId(Long.valueOf(issueVO.getChangelog().getId()));
            changeLogEntity.setChangeType(changeLogItem.getField());
            changeLogEntity.setOriginalValue(subStringText(changeLogItem.getFromString()));
            changeLogEntity.setChangeValue(subStringText(changeLogItem.getToString()));
            if(JiraConstants.LOG_REOPEN.equals(changeLogItem.getToString())){
                changeLogEntity.setChangeStatus(findKeyByCodeAndValue(DictionaryEnum.LOG_CHANGE_STATUS.getName(), changeLogItem.getToString()));
            }
            if(issueVO.getUser() != null){
                changeLogEntity.setOperatorId(issueVO.getUser().getId());
            }
            changeLogEntity.setOperatorDate(new Date());
            if(issueVO.getIssue() != null && StringUtils.isNotEmpty(issueVO.getIssue().getId())){
                changeLogEntity.setIssueId(Long.valueOf(issueVO.getIssue().getId()));
            }
            changeLogMapper.insert(changeLogEntity);

            //监听已标记的时间
            if(JiraConstants.ISSUE_FLAG.equals(changeLogItem.getField()) && issueVO.getIssue() != null
               && !StringUtils.isEmpty(issueVO.getIssue().getId())){
                IssueEntity issueEntity = new IssueEntity();
                issueEntity.setFlagDate(new Date());
                IssueEntity issueCon = new IssueEntity();
                issueCon.setId(Long.valueOf(issueVO.getIssue().getId()));
                Wrapper<IssueEntity> ew = Wrappers.query(issueCon);
                issueMapper.update(issueEntity,ew);
            }
        }

    }

    private void convertProject(String json){
        ProjectVO projectVO = JSONObject.parseObject(json, ProjectVO.class);
        if(projectVO == null || projectVO.getProject() == null){
            return;
        }
        Project project = projectVO.getProject();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(Long.valueOf(project.getId()));
        projectEntity.setKeyValue(project.getKey());
        projectEntity.setName(project.getName());
        if(project.getProjectLead() != null && StringUtils.isNotEmpty(project.getProjectLead().getName())){
            projectEntity.setProjectPerson(project.getProjectLead().getName());

            UserEntity userResult = userMapper.queryUserByName(project.getProjectLead().getName());
            if(userResult != null){
                projectEntity.setProjectPersonId(userResult.getId());
            }
        }

        ProjectEntity checkProject = projectMapper.queryProjectEntityById(projectEntity.getId());
        if(checkProject != null){
            ProjectEntity projectCon = new ProjectEntity();
            projectCon.setId(projectEntity.getId());
            Wrapper<ProjectEntity> ew = Wrappers.query(projectCon);
            projectMapper.update(projectEntity, ew);
        }else{
            projectMapper.insert(projectEntity);
        }
    }

    private void convertSprint(String json){
        SprintVO sprintVO = JSONObject.parseObject(json, SprintVO.class);
        if(sprintVO == null || sprintVO.getSprint() == null){
            return;
        }
        Sprint sprint = sprintVO.getSprint();

        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setId(Long.valueOf(sprint.getId()));
        sprintEntity.setName(sprint.getName());
        if(StringUtils.isNotEmpty(sprint.getState())){
            Integer stateKey = findKeyByCodeAndValue(DictionaryEnum.SPRINT_STATE.getName(), sprint.getState());
            sprintEntity.setState(stateKey);
        }
        if(StringUtils.isNotEmpty(sprint.getStartDate())){
            sprintEntity.setStartDate(convertDateMore(sprint.getStartDate()));
        }
        if(StringUtils.isNotEmpty(sprint.getEndDate())){
            sprintEntity.setEndDate(convertDateMore(sprint.getEndDate()));
        }
        sprintEntity.setOriginBoardId(Long.valueOf(sprint.getOriginBoardId()));
        sprintEntity.setGoal(sprint.getGoal());

        SprintEntity checkSprint = sprintMapper.querySprintById(sprintEntity.getId());
        if(checkSprint != null){
            SprintEntity sprintCon = new SprintEntity();
            sprintCon.setId(sprintEntity.getId());
            Wrapper<SprintEntity> ew = Wrappers.query(sprintCon);
            sprintMapper.update(sprintEntity, ew);
        }else{
            sprintMapper.insert(sprintEntity);
        }

        new Thread(() -> {
            try {
                //保存迭代统计信息
                if(JiraConstants.SPRINT_STATE_CLOSED.equalsIgnoreCase(sprint.getState())){

                    // 延时5s统计，避免issue没有及时update完成
                    try {
                        Thread.sleep(1000l * 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    projectService.saveSprintStatistics(Long.valueOf(sprint.getId()));
                    //保存开发统计历史系信息
                    developerHistoryService.saveDeveloperHistory(Long.valueOf(sprint.getId()));
                    //保存测试统计历史系信息
                    testerHistoryService.saveTesterHistory(Long.valueOf(sprint.getId()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 删除迭代信息
     * @param json
     */
    private void deleteSprint(String json){
        SprintVO sprintVO = JSONObject.parseObject(json, SprintVO.class);
        if(sprintVO == null || sprintVO.getSprint() == null){
            return;
        }
        Sprint sprint = sprintVO.getSprint();
        Long sprintId = Long.valueOf(sprint.getId());

        //删除迭代信息
        SprintEntity sprintCon = new SprintEntity();
        sprintCon.setId(sprintId);
        Wrapper<SprintEntity> ew = Wrappers.query(sprintCon);
        sprintMapper.delete(ew);

        //删除迭代项目信息
        SprintProjectEntity sprintProjectCon = new SprintProjectEntity();
        sprintProjectCon.setSprintId(sprintId);
        Wrapper<SprintProjectEntity> spew = Wrappers.query(sprintProjectCon);
        sprintProjectMapper.delete(spew);

        //删除迭代问题关联表
        IssueSprintEntity issueSprintCon = new IssueSprintEntity();
        issueSprintCon.setSprintId(sprintId);
        Wrapper<IssueSprintEntity> isew = Wrappers.query(issueSprintCon);
        issueSprintMapper.delete(isew);

        //删除迭代统计信息
        SprintStatisticsEntity sprintStatisticsCon = new SprintStatisticsEntity();
        sprintStatisticsCon.setSprintId(sprintId);
        Wrapper<SprintStatisticsEntity> ssew = Wrappers.query(sprintStatisticsCon);
        sprintStatisticsMapper.delete(ssew);
    }

    private Date convertDate(String dateStr){
        return convertDateFormat(dateStr, "yyyy-MM-dd'T'hh:mm:ss.SSSZ");
    }

    private Date convertDateMore(String dateStr){
        return convertDateFormat(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    private Date convertDateFormat(String dateStr, String format){
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }

        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 截取字符串前100
     * @param str
     * @return
     */
    private String subStringText(String str){
        if(StringUtils.isNotEmpty(str) && str.length()>100){
            return str.substring(0, 100);
        }
        return str;
    }
}