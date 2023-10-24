package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity // JPA가 해당클랫를 엔티티로 인식함
public class Question {

    @Id // 기본 키로 지정한다(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 데이터를 저장할 때 해당 속성에 값을 따로 세팅하지 않아도 1씩 자동으로 증가하여 저장된다.
    private Integer id;

    @Column(length = 200)
    // 칼럼의 길이 설정
    private String subject;

    @Column(columnDefinition = "TEXT")
    // 칼럼의 글자수 제한 없애기
    private String content;

    private LocalDateTime createDate;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @ManyToMany
    private Set<SiteUser> voter;
    
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer view;
    // null은 불가능 하며 디폴트 값은 0
}
