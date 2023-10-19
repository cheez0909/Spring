package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	}
	public void create(String subject, String content, SiteUser author) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(author);
		this.questionRepository.save(q);
	}

	// 검색어를 의미하는 kw 매개변수에 추가 
	public Page<Question> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		// 객체를 생성
		Specification<Question> spec = search(kw);
		// 검색결과 리턴
		return this.questionRepository.findAll(spec, pageable);
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}
	private Specification<Question> search(String kw) {
		
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            // Root<Question> q : 기준을 의미하는 Question 엔티티의 객체
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                
                /* 
                 * from question q
                 * left outer join site_user u1 on q.author_id = u1.id
                 */
                // Question, SiteUser 엔티티는 author 속성으로 연결되어 있기 때문에 q.join("author")와 같이 조인
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                
                /* 
                 * from question q
                 * left outer join answer a on q.id=a.question_id
                 */
                // Question과 Answer 엔티티는 "answerList"로 연결되어있다.
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                
                /* 
                 * left outer join site_user u2 on a.author_id = u2.id
                 */
                // Answer, SiteUser 엔티티는 "author"로 연결되어 있다.
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                
                // 입력받은 문자 "kw"를 포함되어 있는 것을 리턴
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 질문 제목 
                        cb.like(q.get("content"), "%" + kw + "%"),      // 질문 내용 
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
            }
        };
    }
}
