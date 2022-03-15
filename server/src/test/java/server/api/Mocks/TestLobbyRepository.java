package server.api.Mocks;

import commons.Lobby;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.LobbyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestLobbyRepository implements LobbyRepository {
    List<Lobby> lobbies = new ArrayList<Lobby>();

    @Override
    public Optional<Lobby> findByToken(String token) {
        for(Lobby l : lobbies){
            if(l.getToken().equals(token))
                return Optional.of(l);
        }
        return Optional.empty();
    }

    @Override
    public List<Lobby> findAll() {
        return lobbies;
    }

    @Override
    public List<Lobby> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Lobby> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Lobby> findAllById(Iterable<Long> longs) {
        return null;  }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public void deleteById(Long aLong) {
        if(lobbies.size() > aLong){
            lobbies.remove(aLong);
        }
    }

    @Override
    public void delete(Lobby entity) {
        if(lobbies.contains(entity))
            lobbies.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Lobby> entities) {
        lobbies = new ArrayList<>();
    }

    @Override
    public void deleteAll() {
        lobbies = new ArrayList<Lobby>();
    }

    @Override
    public <S extends Lobby> S save(S entity) {
        lobbies.add(entity);
        return entity;
    }

    @Override
    public <S extends Lobby> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Lobby> findById(Long aLong) {
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
    public <S extends Lobby> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Lobby> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Lobby> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Lobby getOne(Long aLong) {
        return null;
    }

    @Override
    public Lobby getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Lobby> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Lobby> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Lobby> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Lobby> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Lobby> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Lobby> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Lobby, R> R findBy(Example<S> example,
                                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
