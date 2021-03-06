package emotionalSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

class SortByPriority implements Comparator<Course>
{
    @Override
    public int compare(Course C1 ,Course C2)
    {
        return (C2.Priority - C1.Priority);
    }
}

public class TableGenerator
{
    static int TotalLessons;
    static Random RandomNumber;
    static boolean Failed;
    public static Schedule GenerateTable()
    {
        Failed = false;
        RandomNumber = new Random();
        Schedule NewSchedule = new Schedule();
        ArrayList<Course> ShuffledCourses = new ArrayList<Course>(DataManager.AllCourses.values());
        Collections.shuffle(ShuffledCourses);
        Collections.sort(ShuffledCourses,new SortByPriority());
        TotalLessons = 0;
        for(Course CurrentCourse : ShuffledCourses)
        {
            int LecturesNumber = CurrentCourse.Details.no_of_lecs;
            int SectionsNumeber = CurrentCourse.Details.no_of_sections;
            int LectureHours = CurrentCourse.Details.lec_hrs;
            TotalLessons += LecturesNumber + SectionsNumeber;
            ArrayList<Integer> AddedLectures = new ArrayList<Integer>();
            int RandomLecture = 0;
            for(int j= 0; j < LecturesNumber ; j++)
            {
                String Doctor = CurrentCourse.GetRandomDoctor();
                String Room = CurrentCourse.GetRandomRoom();
                while(AddedLectures.contains(RandomLecture))
                {
                    RandomLecture = RandomNumber.nextInt(SectionsNumeber);                   
                }
                AddedLectures.add(RandomLecture);
                Lesson NewLesson = new Lesson(Doctor, Room, new TimePeriod(LectureHours), Lesson.LessonType.Lecture, CurrentCourse.name,RandomLecture+1,CurrentCourse.Priority);
                TimePeriod LectureTime = NewSchedule.SetOptimalTime(NewLesson);
                NewSchedule.AddLesson(LectureTime.CurrentDay.ordinal(), NewLesson);
            }          
          
        }
        
        for(Course CurrentCourse : ShuffledCourses)
        {
            int SectionsNumeber = CurrentCourse.Details.no_of_sections;
            int SectionHours = (int)CurrentCourse.Details.sec_hrs;
            ArrayList<Integer> AddedSections = new ArrayList<Integer>();
            int RandomSection = 0;
            for(int j= 0; j < SectionsNumeber ; j++)
            {
                String TA = CurrentCourse.GetRandomTA();
                String Room = CurrentCourse.GetRandomRoom();
                while(AddedSections.contains(RandomSection))
                {
                    RandomSection = RandomNumber.nextInt(SectionsNumeber);                   
                }
                AddedSections.add(RandomSection);
                Lesson NewLesson = new Lesson(TA, Room, new TimePeriod(SectionHours), Lesson.LessonType.Section, CurrentCourse.name,RandomSection+1,CurrentCourse.Priority);
                TimePeriod SectionTime =  NewSchedule.SetOptimalTime(NewLesson);
                NewSchedule.AddLesson(SectionTime.CurrentDay.ordinal(), NewLesson);
            }
        }
       
        if(TotalLessons != NewSchedule.Fitness)
        {
            Failed = true;
        }
        return NewSchedule; 
    }   
}
