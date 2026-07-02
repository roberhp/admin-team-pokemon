package com.pokeadmin.adminteampokemon.capture.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pokeadmin.adminteampokemon.capture.dto.CapturePokemonRequest;
import com.pokeadmin.adminteampokemon.capture.dto.CapturePokemonResponse;
import com.pokeadmin.adminteampokemon.capture.entity.CapturedPokemonEntity;
import com.pokeadmin.adminteampokemon.capture.entity.PokemonLocation;
import com.pokeadmin.adminteampokemon.capture.repository.CapturePokemonRepository;
import com.pokeadmin.adminteampokemon.common.exception.PokemonAlreadyExistsException;
import com.pokeadmin.adminteampokemon.common.exception.PokemonStorageFullException;
import com.pokeadmin.adminteampokemon.common.exception.TrainerNotFoundException;
import com.pokeadmin.adminteampokemon.integration.pokemon.client.PokemonClient;
import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;
import com.pokeadmin.adminteampokemon.trainer.entity.TrainerEntity;
import com.pokeadmin.adminteampokemon.trainer.repository.TrainerRepository;

@ExtendWith(MockitoExtension.class)
public class CapturePokemonServiceTest {

    @Mock
    private CapturePokemonRepository capturePokemonRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private PokemonClient pokemonClient;

    @InjectMocks
    private CapturePokemonService capturePokemonService;

    private final String NICKNAME = "Sparky";
    private final Integer NUM_POKEDEX = 25;
    private final String URL_POKEMON = "url";
    private static final long TEAM_LIMIT = 6;
    private static final long STORAGE_LIMIT = 30;
    private static final Long TRAINER_ID = 1L;
    
    @Test
    void capturePokemon_shouldSavePokemon_whenRequestIsValid() {

        // Arrange
        Long trainerId = 1L;
        CapturePokemonRequest request = new CapturePokemonRequest(NUM_POKEDEX,NICKNAME);
        
        TrainerEntity trainer = new TrainerEntity();
        trainer.setTrainerId(trainerId);
        trainer.setUsername("ash");
        trainer.setPassword("123");
        trainer.setTrainerName("Ash Ketchum");

        PokemonInfo pokemonInfo = new PokemonInfo(
            NUM_POKEDEX,
                "pikachu",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png");

        when(trainerRepository.findById(trainerId))
                .thenReturn(Optional.of(trainer));

        when(pokemonClient.findPokemonByPokedexNumber(NUM_POKEDEX))
                .thenReturn(pokemonInfo);

        when(capturePokemonRepository
                .existsByTrainerTrainerIdAndNicknameAndPokedexNumber(
                        trainerId,
                        NICKNAME,
                        NUM_POKEDEX))
                .thenReturn(false);

        when(capturePokemonRepository
                .countByTrainerTrainerIdAndLocation(
                        trainerId,
                        PokemonLocation.TEAM))
                .thenReturn(0L);

        when(capturePokemonRepository.save(any(CapturedPokemonEntity.class)))
                .thenAnswer(invocation -> {
                    CapturedPokemonEntity entity =
                            invocation.getArgument(0);
                    entity.setCapturedPokemonId(1L);
                    return entity;
                });

        // Act
        CapturePokemonResponse response = capturePokemonService.capturePokemon(request, trainerId);

        // Assert
        assertEquals(PokemonLocation.TEAM.name(), response.getLocation()  );
        assertEquals(NICKNAME, response.getNickname());
        verify(trainerRepository).findById(trainerId);
        verify(pokemonClient).findPokemonByPokedexNumber(NUM_POKEDEX);
        verify(capturePokemonRepository)
                .save(any(CapturedPokemonEntity.class));

    }

    @Test
    void capturePokemon_shouldThrowException_whenPokemonAlreadyExists() {

        // Arrange
        Long trainerId = 1L;

        CapturePokemonRequest request = new CapturePokemonRequest(NUM_POKEDEX,NICKNAME);
        

        TrainerEntity trainer = new TrainerEntity();
        trainer.setTrainerId(trainerId);

        when(trainerRepository.findById(trainerId))
                .thenReturn(Optional.of(trainer));

        when(pokemonClient.findPokemonByPokedexNumber(NUM_POKEDEX))
                .thenReturn(new PokemonInfo(
                    NUM_POKEDEX,
                        "pikachu",
                        "https://url"));

        when(capturePokemonRepository
                .existsByTrainerTrainerIdAndNicknameAndPokedexNumber(
                        trainerId,
                        NICKNAME,
                        NUM_POKEDEX))
                .thenReturn(true);

        // Act + Assert

        assertThrows(
                PokemonAlreadyExistsException.class,
                () -> capturePokemonService.capturePokemon(request, trainerId));

        verify(capturePokemonRepository)
                .existsByTrainerTrainerIdAndNicknameAndPokedexNumber(
                        trainerId,
                        NICKNAME,
                        NUM_POKEDEX);

        verify(capturePokemonRepository, org.mockito.Mockito.never())
                .save(any());
    }


    @Test
    void capturePokemon_shouldThrowException_whenTrainerDoesNotExist(){
        // Arrange
        Long trainerId = 0L;

        CapturePokemonRequest request = new CapturePokemonRequest();
        request.setPokedexNumber(NUM_POKEDEX);
        request.setNickname(NICKNAME);

        TrainerEntity trainer = new TrainerEntity();
        trainer.setTrainerId(trainerId);

        when(trainerRepository.findById(trainerId))
                .thenReturn(Optional.of(trainer));

        when(pokemonClient.findPokemonByPokedexNumber(NUM_POKEDEX))
                .thenReturn(new PokemonInfo(
                    NUM_POKEDEX,
                        "pikachu",
                        "https://url"));

        when(capturePokemonRepository
                .existsByTrainerTrainerIdAndNicknameAndPokedexNumber(
                        trainerId,
                        NICKNAME,
                        NUM_POKEDEX))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
            TrainerNotFoundException.class,
            () -> capturePokemonService.capturePokemon(request, trainerId));

    }

    @Test
    void capturePokemon_shouldThrowException_whenTeamIsFull(){
        Long trainerId = 1L;
        //Arrange
        CapturePokemonRequest request = new CapturePokemonRequest();
        request.setNickname(NICKNAME);
        request.setPokedexNumber(NUM_POKEDEX);

        PokemonInfo pokemonInfo = new PokemonInfo(NUM_POKEDEX, NICKNAME, URL_POKEMON);
        
        TrainerEntity trainer = new TrainerEntity();
        trainer.setTrainerId(trainerId);

        when(pokemonClient.findPokemonByPokedexNumber(NUM_POKEDEX)).thenReturn(pokemonInfo);

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        when(capturePokemonRepository.countByTrainerTrainerIdAndLocation(trainerId,PokemonLocation.TEAM)).thenReturn(TEAM_LIMIT);
        when(capturePokemonRepository.countByTrainerTrainerIdAndLocation(trainerId,PokemonLocation.STORAGE)).thenReturn(STORAGE_LIMIT);


        //act
        assertThrows(PokemonStorageFullException.class, ()-> capturePokemonService.capturePokemon(request, trainerId));
    }

    @Test
    void capturePokemon_shouldAssignPokemonToBox_whenTeamIsFull(){
        // Arrange
        CapturePokemonRequest request = new CapturePokemonRequest(NUM_POKEDEX,NICKNAME);
        

        TrainerEntity trainer = new TrainerEntity();
        trainer.setTrainerId(TRAINER_ID);

        PokemonInfo pokemonInfo = new PokemonInfo(NUM_POKEDEX, NICKNAME, URL_POKEMON);


        when(pokemonClient.findPokemonByPokedexNumber(NUM_POKEDEX)).thenReturn(pokemonInfo);
        when(trainerRepository.findById(TRAINER_ID)).thenReturn(Optional.of(trainer));
        when(capturePokemonRepository.countByTrainerTrainerIdAndLocation(TRAINER_ID,PokemonLocation.TEAM)).thenReturn(TEAM_LIMIT);
        when(capturePokemonRepository.existsByTrainerTrainerIdAndNicknameAndPokedexNumber(TRAINER_ID, NICKNAME, NUM_POKEDEX)) .thenReturn(false);
        when (capturePokemonRepository.save(any(CapturedPokemonEntity.class)))
            .thenAnswer(invocation -> {
                CapturedPokemonEntity entity = invocation.getArgument(0);
                entity.setCapturedPokemonId(1l);
                return entity;
            });
        
        // act
        CapturePokemonResponse response = capturePokemonService.capturePokemon(request, TRAINER_ID);

            // Assert
            assertEquals(PokemonLocation.STORAGE.name(), response.getLocation()  );
            assertEquals(NICKNAME, response.getNickname());
            verify(trainerRepository).findById(TRAINER_ID);
            verify(pokemonClient).findPokemonByPokedexNumber(NUM_POKEDEX);
            verify(capturePokemonRepository)
                    .save(any(CapturedPokemonEntity.class));
    
    

    }
}
