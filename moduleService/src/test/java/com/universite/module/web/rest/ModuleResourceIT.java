package com.universite.module.web.rest;

import static com.universite.module.domain.ModuleAsserts.*;
import static com.universite.module.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.module.IntegrationTest;
import com.universite.module.domain.Module;
import com.universite.module.repository.ModuleRepository;
import com.universite.module.service.dto.ModuleDTO;
import com.universite.module.service.mapper.ModuleMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ModuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModuleResourceIT {

    private static final Integer DEFAULT_ID_MODULE = 1;
    private static final Integer UPDATED_ID_MODULE = 2;

    private static final Integer DEFAULT_CREDIT = 1;
    private static final Integer UPDATED_CREDIT = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModuleMockMvc;

    private Module module;

    private Module insertedModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createEntity() {
        return new Module().idModule(DEFAULT_ID_MODULE).credit(DEFAULT_CREDIT).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Module createUpdatedEntity() {
        return new Module().idModule(UPDATED_ID_MODULE).credit(UPDATED_CREDIT).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        module = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedModule != null) {
            moduleRepository.delete(insertedModule);
            insertedModule = null;
        }
    }

    @Test
    @Transactional
    void createModule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);
        var returnedModuleDTO = om.readValue(
            restModuleMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moduleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModuleDTO.class
        );

        // Validate the Module in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModule = moduleMapper.toEntity(returnedModuleDTO);
        assertModuleUpdatableFieldsEquals(returnedModule, getPersistedModule(returnedModule));

        insertedModule = returnedModule;
    }

    @Test
    @Transactional
    void createModuleWithExistingId() throws Exception {
        // Create the Module with an existing ID
        module.setId(1L);
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModuleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdModuleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        module.setIdModule(null);

        // Create the Module, which fails.
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        restModuleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModules() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        // Get all the moduleList
        restModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].idModule").value(hasItem(DEFAULT_ID_MODULE)))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getModule() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.idModule").value(DEFAULT_ID_MODULE))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModule() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the module
        Module updatedModule = moduleRepository.findById(module.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModule are not directly saved in db
        em.detach(updatedModule);
        updatedModule.idModule(UPDATED_ID_MODULE).credit(UPDATED_CREDIT).name(UPDATED_NAME);
        ModuleDTO moduleDTO = moduleMapper.toDto(updatedModule);

        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moduleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModuleToMatchAllProperties(updatedModule);
    }

    @Test
    @Transactional
    void putNonExistingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moduleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.idModule(UPDATED_ID_MODULE).credit(UPDATED_CREDIT).name(UPDATED_NAME);

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModuleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedModule, module), getPersistedModule(module));
    }

    @Test
    @Transactional
    void fullUpdateModuleWithPatch() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the module using partial update
        Module partialUpdatedModule = new Module();
        partialUpdatedModule.setId(module.getId());

        partialUpdatedModule.idModule(UPDATED_ID_MODULE).credit(UPDATED_CREDIT).name(UPDATED_NAME);

        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModule))
            )
            .andExpect(status().isOk());

        // Validate the Module in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModuleUpdatableFieldsEquals(partialUpdatedModule, getPersistedModule(partialUpdatedModule));
    }

    @Test
    @Transactional
    void patchNonExistingModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moduleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        module.setId(longCount.incrementAndGet());

        // Create the Module
        ModuleDTO moduleDTO = moduleMapper.toDto(module);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModuleMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Module in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModule() throws Exception {
        // Initialize the database
        insertedModule = moduleRepository.saveAndFlush(module);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the module
        restModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, module.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moduleRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Module getPersistedModule(Module module) {
        return moduleRepository.findById(module.getId()).orElseThrow();
    }

    protected void assertPersistedModuleToMatchAllProperties(Module expectedModule) {
        assertModuleAllPropertiesEquals(expectedModule, getPersistedModule(expectedModule));
    }

    protected void assertPersistedModuleToMatchUpdatableProperties(Module expectedModule) {
        assertModuleAllUpdatablePropertiesEquals(expectedModule, getPersistedModule(expectedModule));
    }
}
