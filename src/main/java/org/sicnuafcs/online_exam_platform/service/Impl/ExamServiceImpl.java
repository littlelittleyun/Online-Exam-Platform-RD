package org.sicnuafcs.online_exam_platform.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.dao.*;
import org.sicnuafcs.online_exam_platform.model.*;
import org.sicnuafcs.online_exam_platform.service.ExamService;
import org.sicnuafcs.online_exam_platform.service.QuestionService;
import org.sicnuafcs.online_exam_platform.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@EnableAsync
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    @Autowired
    StuExamRepository stuExamRepository;
    @Autowired
    StudentRepository studentRepository;

    @Override
    public long saveToExam(Exam exam) throws Exception {
        //编辑试卷信息
        if (exam.getExam_id() != 0) {
            examRepository.save(exam);
            return exam.getExam_id();
        } else {
            long exam_id = redisUtils.incr("exam_id");
            exam.setExam_id(exam_id);
            examRepository.save(exam);
            return exam_id;
        }
    }

    @Override
    public void saveQuestionToExam(ExamQuestion examQuestion) throws Exception {
        examQuestionRepository.save(examQuestion);
    }

    public void distributeExamToStudent(long exma_id) throws Exception {
        ArrayList<Student> studentList = (ArrayList<Student>) studentRepository.findAll();
        ArrayList<ExamQuestion> singleList = examQuestionRepository.findByType(Question.Type.Single);
        ArrayList<ExamQuestion> judgeList = examQuestionRepository.findByType(Question.Type.Judge);
        for (Student student : studentList) {
            randomCommon(singleList);
            randomCommon(judgeList);
            StuExam stuExam = new StuExam();
            for (ExamQuestion single : singleList) {
                stuExam.setStu_id(student.getStu_id());
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(single.getQuestion_id());
                stuExamRepository.save(stuExam);
            }
            for (ExamQuestion judge : judgeList) {
                stuExam.setStu_id(student.getStu_id());
                stuExam.setExam_id(exma_id);
                stuExam.setQuestion_id(judge.getQuestion_id());
                stuExamRepository.save(stuExam);
            }
        }

    }
    public static void randomCommon(ArrayList<ExamQuestion> questionsList) {
        int n = questionsList.size();
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (n - 1)) + 1;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == questionsList.get(j).getNumber()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                questionsList.get(count).setNumber(num);
                count++;
            }
        }
    }
}
