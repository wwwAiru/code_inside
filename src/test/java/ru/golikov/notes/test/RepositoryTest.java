package ru.golikov.notes.test;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.transaction.annotation.Transactional;
import ru.golikov.notes.AbstractSpringBootTest;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.role.entity.Role;
import ru.golikov.notes.domain.role.repository.RoleRepository;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.domain.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class RepositoryTest extends AbstractSpringBootTest {

    private static boolean initDone = false;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditReader auditReader;

    @BeforeEach
    void initSql() throws SQLException {
        if (initDone) return;
        initDone = true;
        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        Connection connection = Objects.requireNonNull(info.getDataSource()).getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/users.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/notes.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/roles.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/user_roles.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/custom_revision_entity.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/users_aud.sql"));
        ScriptUtils.executeSqlScript(connection, new ClassPathResource("data/notes_aud.sql"));
    }
            

    @Test
    public void userRepository_findAllTest() {
        List<User> userList = userRepository.findAll();
        assertThat(userList).isNotEmpty();
        assertThat(userList.size()).isEqualTo(4);
    }

    @Test
    public void userRepository_findByIdTest() {
        Optional<User> userOptional = userRepository.findById(1L);
        User user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo("admin@mail.ru");
    }

    @Test
    public void userRepository_findByEmailTest() {
        Optional<User> userOptional = userRepository.findByEmail("sergeevich@aa.ru");
        User user = userOptional.get();
        assertThat(user.getId()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void userRepository_deleteByIdTest() {
        userRepository.deleteById(8L);
        assertThat(userRepository.findById(8L)).isEmpty();
    }

    @Test
    public void noteRepository_findByIdAndUserIdTest() {
        Note note = noteRepository.findByIdAndUserId(2L, 1L).get();
        assertThat(note).isNotNull();
        assertThat(note.getTitle()).isEqualTo("Admin");
    }

    @Test
    public void noteRepository_findByIdTest() {
        Note note = noteRepository.findById(3L).get();
        assertThat(note).isNotNull();
        assertThat(note.getTitle()).isEqualTo("Заметка 1");
    }

    @Test
    public void noteRepository_findAllByUserTest(){
        User user = new User();
        user.setId(3L);
        Page<Note> allByUser = noteRepository.findAllByUser(user, PageRequest.of(0, 4));
        assertThat(allByUser.getTotalElements()).isEqualTo(4);
        assertThat(allByUser.getTotalPages()).isEqualTo(1);
        assertThat(allByUser.toList()).isNotEmpty();
    }

    @Test
    @Transactional
    public void noteRepository_deleteTest() {
        Note note = new Note();
        note.setId(6L);
        noteRepository.delete(note);
        assertThat(noteRepository.findById(6L)).isEmpty();
    }

    @Test
    public void roleRepository_findByRoleTest() {
        Role roleUser = roleRepository.findByRole("ROLE_USER").get();
        assertThat(roleUser.getId()).isEqualTo(2L);
    }

    @Test
    public void roleRepository_findAllTest() {
        List<Role> all = roleRepository.findAll();
        assertThat(all.size()).isEqualTo(2L);
        assertThat(all.get(0).getRole()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void AuditUserTest() {
        List<?> resultList = auditReader.createQuery().forRevisionsOfEntity(User.class, true)
                .add(AuditEntity.id().eq(8L))
                .getResultList();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.size()).isEqualTo(15);
        assertThat(resultList.get(0)).isNotNull();
    }


}
