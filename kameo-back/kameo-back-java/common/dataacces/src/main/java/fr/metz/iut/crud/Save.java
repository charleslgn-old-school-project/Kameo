package fr.metz.iut.crud;

import fr.metz.iut.dataacces.PersistentStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Save an object using the DAO of the object
 */
public interface Save {

  static Save get() {
    return StandardSave.SaveSingleton.INSTANCE.save;
  }

  <T extends PersistentStorage> T save(T t);

  default <T extends PersistentStorage> List<T> save(T... ts) {
    return Stream.of(ts).map(this::save).toList();
  }

  default <T extends PersistentStorage> List<T> save(Collection<T> ts) {
    return ts.stream().map(this::save).toList();
  }
}
