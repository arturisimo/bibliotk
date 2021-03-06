package apz.btk.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import apz.btk.entity.Libro;

@RepositoryRestResource(collectionResourceRel = "libros", path = "libros")
public interface LibroRepository extends CrudRepository<Libro, Long> {

	List<Libro> findByAlta(boolean alta);
}