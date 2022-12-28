package com.expmanagment.repo;

import com.expmanagment.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.expmanagment.entity.TakesEntity;
import com.expmanagment.entity.FinishesEntity;

import javax.transaction.Transactional;


/**
 * TaskId的命名规范：
 * 前三位: 课程ID
 * 第四位: 是否为大型实验项目或小型试验项目(
 *        大型实验项目该位为1
 *        小型为0 )
 * 后两位: 在当前课程下的实验序号（大型/小型分开计算）
 *      e.g. 102001和102101可以共存。
 * e.g. 主机路由实验:
 *          课程: 计算机网络实验（课程ID102）
 *          类型: 小型实验项目，在小型实验项目里编号为05
 *          主机路由实验的taskId为102005
 */
public interface TaskRepo extends JpaRepository<TaskEntity, Integer> {

    @Query(value = "select t from TaskEntity t where t.courseId = ?1")
    public Optional<List<TaskEntity>> findByCourseId(Integer courseId);

    @Query(value = "select t from TaskEntity t where t.courseId = ?1 and t.type = ?2")
    public Optional<List<TaskEntity>> findByCourseIdAndType(Integer courseId,Integer type);

    @Query(value = "select t2.studentId, t2.courseId, c.name as courseName, " +
            "t1.id as taskId, t1.name as taskName, t1.deadline, t1.type " +
            "from TaskEntity t1, TakesEntity t2, CourseEntity c " +
            "where t1.courseId = t2.courseId and t1.courseId = c.id " +
            "and t2.studentId = ?1 " +
            "and not exists " +
            "(select f from FinishesEntity f where f.finished = 1 and f.taskId = t1.id and f.studentId = t2.studentId) " +
            "order by t1.deadline asc nulls last")
    public Optional<List<Object>> findAllByCourseIdAndUnfinishedOrderByDeadlineAsc(Integer studentId);

    @Query(value = "select t2.studentId, t2.courseId, c.name as courseName, " +
            "t1.id as taskId, t1.name as taskName, t1.deadline, t1.type " +
            "from TaskEntity t1, TakesEntity t2, CourseEntity c, FinishesEntity f " +
            "where t1.courseId = t2.courseId and t1.courseId = c.id " +
            "and t2.studentId = ?1 " +
            "and t2.studentId = f.studentId and t1.id = f.taskId " +
            "and f.finished = 1 " +
            "order by t1.deadline asc nulls last")
    public Optional<List<Object>> findAllByCourseIdAndFinishedOrderByDeadlineAsc(Integer studentId);

    /**
     * 修改task的详细信息
     * @param id
     * @param courseId
     * @param name
     * @param description
     * @param deadline
     * @param type
     * @param url
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update TaskEntity s set s.courseId = ?2,s.name=?3,s.description=?4,s.deadline =?5,s.type=?6 ,s.url=?7 where s.id = ?1")
    void updateTaskInformation(int id, Integer courseId, String name, String description, Date deadline, Integer type, String url);

}
