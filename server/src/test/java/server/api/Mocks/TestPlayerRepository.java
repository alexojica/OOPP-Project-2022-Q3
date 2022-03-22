package server.api.Mocks;

import commons.Player;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestPlayerRepository implements PlayerRepository {

    List<Player> players = new ArrayList<Player>();
    @Override
    public List<Player> findAll() {
        return players;
    }

    @Override
    public List<Player> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Player> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return players.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for(Player p : players){
            if(p.id == aLong)
                players.remove(p);
        }
    }

    @Override
    public void delete(Player entity) {
        if(players.contains(entity))
            players.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {

    }

    @Override
    public void deleteAll() {
        players = new ArrayList<Player>();
    }

    @Override
    public <S extends Player> S save(S entity) {
        players.add(entity);
        return entity;
    }

    @Override
    public <S extends Player> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Player> findById(Long aLong) {
        for(Player p : players){
            if(p.id == aLong)
                return Optional.of(p);
        }
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
    public <S extends Player> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Player> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Player> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Player getOne(Long aLong) {
        return null;
    }

    @Override
    public Player getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Player> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Player> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Player> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Player, R> R findBy(Example<S> example,
                                          Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public void updateScore(int score, long id){
        //temporary to avoid error
    }
}
