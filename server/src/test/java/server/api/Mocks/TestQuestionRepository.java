package server.api.Mocks;

import commons.Question;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestQuestionRepository implements QuestionRepository {

    private List<Question> questions = new ArrayList<Question>();

    @Override
    public Optional<Question> findById(Long pointer) {
        for(Question q : questions){
            if(q.id == pointer)
                return Optional.of(q);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Optional<Question> findByIdAndLastLobbyToken(Long pointer, String lastLobby) {
        return Optional.empty();
    }

    @Override
    public long count() {
        return questions.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for(Question q : questions){
            if(q.id == aLong)
                questions.remove(q);
        }
    }

    @Override
    public void delete(Question entity) {
        if(questions.contains(entity))
            questions.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Question> entities) {

    }

    @Override
    public void deleteAll() {
        questions = new ArrayList<Question>();
    }

    @Override
    public List<Question> findAll() {
        return questions;
    }

    @Override
    public List<Question> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Question> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Question> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends Question> S save(S entity) {
        questions.add(entity);
        return entity;
    }

    @Override
    public <S extends Question> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Question> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Question> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Question> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Question getOne(Long aLong) {
        return null;
    }

    @Override
    public Question getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Question> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Question> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Question> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Question> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Question> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Question> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Question, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
