package server.api.Mocks;

import commons.LeaderboardEntry;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.LeaderboardRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestLeaderboardRepository implements LeaderboardRepository {

    List<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();

    @Override
    public List<LeaderboardEntry> findTop10ByOrderByScoreDesc() {
        List<Integer> scores = new ArrayList<Integer>();
        List<LeaderboardEntry> leaderboardEntries1 = new ArrayList<LeaderboardEntry>();
        int i = 0;
        for(LeaderboardEntry l : leaderboardEntries){
            scores.add(l.getScore());
        }
        Collections.sort(scores, Collections.reverseOrder());
        for(LeaderboardEntry l : leaderboardEntries){
            if(scores.get(i) == l.getScore()){
                leaderboardEntries1.add(i, l);
            }
        }
        return leaderboardEntries1.stream().limit(10).collect(Collectors.toList());
    }

    @Override
    public Optional<LeaderboardEntry> findByName(String name) {
        for(LeaderboardEntry l : leaderboardEntries){
            if(l.getName().equals(name)){
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }


    @Override
    public List<LeaderboardEntry> findAll() {
        return leaderboardEntries;
    }

    @Override
    public List<LeaderboardEntry> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<LeaderboardEntry> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<LeaderboardEntry> findAllById(Iterable<Long> longs) {
        return null;  }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public void deleteById(Long aLong) {
        if(leaderboardEntries.size() > aLong){
            leaderboardEntries.remove(aLong);
        }
    }

    @Override
    public void delete(LeaderboardEntry entity) {
        if(leaderboardEntries.contains(entity))
            leaderboardEntries.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends LeaderboardEntry> entities) {
        leaderboardEntries = new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        leaderboardEntries = new ArrayList<LeaderboardEntry>();
    }

    @Override
    public <S extends LeaderboardEntry> S save(S entity) {
        leaderboardEntries.add(entity);
        return entity;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<LeaderboardEntry> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends LeaderboardEntry> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<LeaderboardEntry> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public LeaderboardEntry getOne(Long aLong) {
        return null;
    }

    @Override
    public LeaderboardEntry getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends LeaderboardEntry> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends LeaderboardEntry> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends LeaderboardEntry, R> R findBy(Example<S> example,
                                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
