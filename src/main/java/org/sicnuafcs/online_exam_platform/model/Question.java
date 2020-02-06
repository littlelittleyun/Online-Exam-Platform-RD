package org.sicnuafcs.online_exam_platform.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Question")

public class Question {
    public static enum Type {
        Single,
        MultipleChoice,
        Judge,
        FillInTheBlank,
        Discussion,
        Program;
    }

    @Id
    @Column(length = 16, nullable = false)
    private long question_id;

    @Column(length = 16, nullable = false)
    @NotBlank(message = "教师id不为空")
    private  String tea_id;

    @Column(length = 16, nullable = false)
    @NotBlank(message = "题目不为空")
    private String question;

    @Column(length = 128, nullable = false)
    @NotBlank(message = "标签不为空")
    private String tag;

    @Column(length = 256)
    private  String options;

    @Column(nullable = false)
    @NotBlank(message = "答案不为空")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank(message = "类型不为空")
    private Type type;

    @Column
    private String input;

    @Column
    private String output;
}
