package pt.ul.fc.css.soccernow;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.entities.Card;
import pt.ul.fc.css.soccernow.business.entities.ChampionshipByPoints;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.entities.Stats.GameResult;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.repositories.CardRepository;
import pt.ul.fc.css.soccernow.business.repositories.ChampionshipRepository;
import pt.ul.fc.css.soccernow.business.repositories.GameRepository;
import pt.ul.fc.css.soccernow.business.repositories.PlayerRepository;
import pt.ul.fc.css.soccernow.business.repositories.RefereeRepository;
import pt.ul.fc.css.soccernow.business.repositories.RefereeTeamRepository;
import pt.ul.fc.css.soccernow.business.repositories.StartingTeamRepository;
import pt.ul.fc.css.soccernow.business.repositories.StatsRepository;
import pt.ul.fc.css.soccernow.business.repositories.TeamRepository;
import pt.ul.fc.css.soccernow.business.repositories.UserRepository;

/**
 * Creates: - 10 players and 2 goalkeepers - 1 main referee and 2 referees - 2 teams - 2 starting
 * teams - 2 games - 2 stats, 1 for each game - 3 cards, 1 red and 2 yellow
 */
@Component
public class DBInitialization {

  @Autowired private CardRepository cardRepository;

  @Autowired private TeamRepository teamRepository;

  @Autowired private PlayerRepository playerRepository;

  @Autowired private RefereeRepository refereeRepository;

  @Autowired private RefereeTeamRepository refereeTeamRepository;

  @Autowired private GameRepository gameRepository;

  @Autowired private StatsRepository statsRepository;

  @Autowired private StartingTeamRepository startingTeamRepository;

  @Autowired private ChampionshipRepository championshipRepository;

  @Autowired private UserRepository userRepository;

  private Player 
      player1,
      player2,
      player3,
      player4,
      player5,
      player6,
      player7,
      player8,
      player9,
      player10,
      player11,
      player12,
      player13,
      player14,
      player15,
      player16,
      player17,
      player18,
      player19,
      player20,
      player21,
      player22,
      player23,
      player24,
      player25,
      player26,
      player27,
      player28,
      player29,
      player30,
      player31,
      player32;
  private Player goalkeeper1, goalkeeper2, goalkeeper3, goalkeeper4, goalkeeper5, goalkeeper6, goalkeeper7, goalkeeper8;
  private Referee mainRef, referee1, referee2;
  private RefereeTeam refereeTeam1, refereeTeam2, refereeTeam3;
  private Team team1, team2, team3, team4, team5, team6, team7, team8;
  private StartingTeam startingTeam1, startingTeam2, startingTeam3, startingTeam4, startingTeam5, startingTeam6, startingTeam7, startingTeam8;
  private Game game1, game2, game3;

  @PostConstruct
  @Order(1)
  private void initDatabase() {
    generatePlayers();
    generateReferees();
    generateRefereeTeam();
    generateTeams();
    generateGameWithStatsAndLineups();
    generateChampionshipByPointss();
  }

  private void generatePlayers() {
    List<Player> players = new ArrayList<>();
    player1 = new Player("Lionel", "Messi", PlayerPosition.OTHER, null, null);
    player2 = new Player("Cristiano", "Ronaldo", PlayerPosition.OTHER, null, null);
    player3 = new Player("Neymar", "Jr", PlayerPosition.OTHER, null, null);
    player4 = new Player("Carlos", "Costa", PlayerPosition.OTHER, null, null);
    player5 = new Player("Maria", "Oliveira", PlayerPosition.OTHER, null, null);
    player6 = new Player("Rui", "Almeida", PlayerPosition.OTHER, null, null);
    player7 = new Player("Beatriz", "Fernandes", PlayerPosition.OTHER, null, null);
    player8 = new Player("Miguel", "Rodrigues", PlayerPosition.OTHER, null, null);
    player9 = new Player("Tiago", "Martins", PlayerPosition.OTHER, null, null);
    player10 = new Player("Sofia", "Gomes", PlayerPosition.OTHER, null, null);
    player11 = new Player("André", "Pereira", PlayerPosition.OTHER, null, null);
    player12 = new Player("Ana", "Silva", PlayerPosition.OTHER, null, null);
    player13 = new Player("Joana", "Moutinho", PlayerPosition.OTHER, null, null);
    player14 = new Player("Bernarda", "Silva", PlayerPosition.OTHER, null, null);
    player15 = new Player("Patrícia", "Ferreira", PlayerPosition.OTHER, null, null);
    player16 = new Player("Rafaela", "Leão", PlayerPosition.OTHER, null, null);
    player17 = new Player("Diogo", "Jota", PlayerPosition.OTHER, null, null);
    player18 = new Player("Mariana", "Fernandes", PlayerPosition.OTHER, null, null);
    player19 = new Player("João", "Félix", PlayerPosition.OTHER, null, null);
    player20 = new Player("Carla", "Horta", PlayerPosition.OTHER, null, null);
    player21 = new Player("Gonçalo", "Ramos", PlayerPosition.OTHER, null, null);
    player22 = new Player("Pedro", "Gonçalves", PlayerPosition.OTHER, null, null);
    player23 = new Player("Vitinha", "Ferreira", PlayerPosition.OTHER, null, null);
    player24 = new Player("Otávia", "Monteiro", PlayerPosition.OTHER, null, null);
    player25 = new Player("Nuno", "Mendes", PlayerPosition.OTHER, null, null);
    player26 = new Player("Rúben", "Dias", PlayerPosition.OTHER, null, null);
    player27 = new Player("Daniela", "Pereira", PlayerPosition.OTHER, null, null);
    player28 = new Player("João", "Cancelo", PlayerPosition.OTHER, null, null);
    player29 = new Player("Raphael", "Guerreiro", PlayerPosition.OTHER, null, null);
    player30 = new Player("Wilma", "Carvalho", PlayerPosition.OTHER, null, null);
    player31 = new Player("Andreia", "Silva", PlayerPosition.OTHER, null, null); 
    player32 = new Player("Gonçalo", "Inácio", PlayerPosition.OTHER, null, null);

    goalkeeper1 = new Player("Manuel", "Avila", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper2 = new Player("Jorge", "Espinola", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper3 = new Player("Rui", "Patricio", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper4 = new Player("Inês", "Matos", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper5 = new Player("Cláudia", "Ramos", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper6 = new Player("Ricardo", "Pereira", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper7 = new Player("Eduardo", "Moreira", PlayerPosition.GOALKEEPER, null, null);
    goalkeeper8 = new Player("Helena", "Sousa", PlayerPosition.GOALKEEPER, null, null);

    players.addAll(Arrays.asList(
        player1, player2, player3, player4, player5, player6, player7, player8, player9, player10,
        player11, player12, player13, player14, player15, player16, player17, player18, player19, player20,
        player21, player22, player23, player24, player25, player26, player27, player28, player29, player30,
        player31, player32,
        goalkeeper1, goalkeeper2, goalkeeper3, goalkeeper4, goalkeeper5, goalkeeper6, goalkeeper7, goalkeeper8
    ));

    // Save all as users first
    userRepository.saveAll(players);

    // Then save as players
    playerRepository.saveAll(players);
  }

  private void generateReferees() {
    mainRef = new Referee("Carlos", "Xistra", true, null);
    referee1 = new Referee("Pedro", "Proença", true, null);
    referee2 = new Referee("Manel", "Proença", false, null);

    userRepository.saveAll(new ArrayList<>(Arrays.asList(mainRef, referee1, referee2)));
    // Save all as referees
    refereeRepository.saveAll(new ArrayList<>(Arrays.asList(mainRef, referee1, referee2)));
  }

  public void generateRefereeTeam() {
    refereeTeam1 =
        new RefereeTeam(false, mainRef, new ArrayList<>(List.of(referee1)), new ArrayList<>());
    refereeTeam2 =
        new RefereeTeam(
            false, mainRef, new ArrayList<>(List.of(referee1, referee2)), new ArrayList<>());
    refereeTeam3 =
        new RefereeTeam(true, mainRef, new ArrayList<>(List.of(referee1)), new ArrayList<>());
    refereeTeamRepository.saveAll(
        new ArrayList<>(Arrays.asList(refereeTeam1, refereeTeam2, refereeTeam3)));
  }

  public void generateTeams() {
    team1 = new Team("FC Porto");
    team1.setPlayers(
        new ArrayList<>(Arrays.asList(player1, player2, player3, player4, goalkeeper1)));
    startingTeam1 =
        new StartingTeam(
            team1, new ArrayList<>(Arrays.asList(player1, player2, player3, player4)), goalkeeper1);
    player1.addStartingTeam(startingTeam1);
    player2.addStartingTeam(startingTeam1);
    player3.addStartingTeam(startingTeam1);
    player4.addStartingTeam(startingTeam1);
    goalkeeper1.addStartingTeam(startingTeam1);

    team2 = new Team("Sporting CP");
    team2.setPlayers(
        new ArrayList<>(Arrays.asList(player5, player6, player7, player8, goalkeeper2)));
    startingTeam2 =
        new StartingTeam(
            team2, new ArrayList<>(Arrays.asList(player5, player6, player7, player8)), goalkeeper2);
    player5.addStartingTeam(startingTeam2);
    player6.addStartingTeam(startingTeam2);
    player7.addStartingTeam(startingTeam2);
    player8.addStartingTeam(startingTeam2);
    goalkeeper2.addStartingTeam(startingTeam2);

    team3 = new Team("SL Benfica");
    team3.setPlayers(
        new ArrayList<>(Arrays.asList(player9, player10, player11, player12, goalkeeper3)));
    startingTeam3 =
        new StartingTeam(
            team3,
            new ArrayList<>(Arrays.asList(player9, player10, player11, player12)),
            goalkeeper3);
    player9.addStartingTeam(startingTeam3);
    player10.addStartingTeam(startingTeam3);
    player11.addStartingTeam(startingTeam3);
    player12.addStartingTeam(startingTeam3);
    goalkeeper3.addStartingTeam(startingTeam3);

    team4 = new Team("FC Belenences");
    team4.setPlayers(
        new ArrayList<>(Arrays.asList(player13, player14, player15, player16, goalkeeper4)));
    startingTeam4 =
        new StartingTeam(
            team4, new ArrayList<>(Arrays.asList(player13, player14, player15, player16)), goalkeeper4);
    player13.addStartingTeam(startingTeam4);
    player14.addStartingTeam(startingTeam4);
    player15.addStartingTeam(startingTeam4);
    player16.addStartingTeam(startingTeam4);
    goalkeeper4.addStartingTeam(startingTeam4);

    team5 = new Team("Boavista FC");
    team5.setPlayers(
        new ArrayList<>(Arrays.asList(player17, player18, player19, player20, goalkeeper5)));
    startingTeam5 =
        new StartingTeam(
            team5, new ArrayList<>(Arrays.asList(player17, player18, player19, player20)), goalkeeper5);
    player17.addStartingTeam(startingTeam5);
    player18.addStartingTeam(startingTeam5);
    player19.addStartingTeam(startingTeam5);
    player20.addStartingTeam(startingTeam5);
    goalkeeper5.addStartingTeam(startingTeam5);

    team6 = new Team("Vitória SC");
    team6.setPlayers(
        new ArrayList<>(Arrays.asList(player21, player22, player23, player24, goalkeeper6)));
    startingTeam6 =
        new StartingTeam(
            team6, new ArrayList<>(Arrays.asList(player21, player22, player23, player24)), goalkeeper6);
    player21.addStartingTeam(startingTeam6);
    player22.addStartingTeam(startingTeam6);
    player23.addStartingTeam(startingTeam6);
    player24.addStartingTeam(startingTeam6);
    goalkeeper6.addStartingTeam(startingTeam6);

    team7 = new Team("SC Braga");
    team7.setPlayers(
        new ArrayList<>(Arrays.asList(player25, player26, player27, player28, goalkeeper7)));
    startingTeam7 =
        new StartingTeam(
            team7, new ArrayList<>(Arrays.asList(player25, player26, player27, player28)), goalkeeper7);
    player25.addStartingTeam(startingTeam7);
    player26.addStartingTeam(startingTeam7);
    player27.addStartingTeam(startingTeam7);
    player28.addStartingTeam(startingTeam7);
    goalkeeper7.addStartingTeam(startingTeam7);

    team8 = new Team("Rio Ave FC");
    team8.setPlayers(
        new ArrayList<>(Arrays.asList(player29, player30, player31, player32, goalkeeper8)));
    startingTeam8 =
        new StartingTeam(
            team8, new ArrayList<>(Arrays.asList(player29, player30, player31, player32)), goalkeeper8);
    player29.addStartingTeam(startingTeam8);
    player30.addStartingTeam(startingTeam8);
    player31.addStartingTeam(startingTeam8);
    player32.addStartingTeam(startingTeam8);
    goalkeeper8.addStartingTeam(startingTeam8);

    teamRepository.saveAll(new ArrayList<>(Arrays.asList(
        team1, team2, team3, team4, team5, team6, team7, team8)));
    startingTeamRepository.saveAll(new ArrayList<>(Arrays.asList(
        startingTeam1, startingTeam2, startingTeam3, startingTeam4,
        startingTeam5, startingTeam6, startingTeam7, startingTeam8)));
    // Add teams to players
    player1.setTeams(new ArrayList<>(Arrays.asList(team1)));
    player2.setTeams(new ArrayList<>(Arrays.asList(team1)));
    player3.setTeams(new ArrayList<>(Arrays.asList(team1)));
    player4.setTeams(new ArrayList<>(Arrays.asList(team1)));
    player5.setTeams(new ArrayList<>(Arrays.asList(team2)));
    player6.setTeams(new ArrayList<>(Arrays.asList(team2)));
    player7.setTeams(new ArrayList<>(Arrays.asList(team2)));
    player8.setTeams(new ArrayList<>(Arrays.asList(team2)));
    player9.setTeams(new ArrayList<>(Arrays.asList(team3)));
    player10.setTeams(new ArrayList<>(Arrays.asList(team3)));
    player11.setTeams(new ArrayList<>(Arrays.asList(team3)));
    player12.setTeams(new ArrayList<>(Arrays.asList(team3)));
    player13.setTeams(new ArrayList<>(Arrays.asList(team4)));
    player14.setTeams(new ArrayList<>(Arrays.asList(team4)));
    player15.setTeams(new ArrayList<>(Arrays.asList(team4)));
    player16.setTeams(new ArrayList<>(Arrays.asList(team4)));
    player17.setTeams(new ArrayList<>(Arrays.asList(team5)));
    player18.setTeams(new ArrayList<>(Arrays.asList(team5)));
    player19.setTeams(new ArrayList<>(Arrays.asList(team5)));
    player20.setTeams(new ArrayList<>(Arrays.asList(team5)));
    player21.setTeams(new ArrayList<>(Arrays.asList(team6)));
    player22.setTeams(new ArrayList<>(Arrays.asList(team6)));
    player23.setTeams(new ArrayList<>(Arrays.asList(team6)));
    player24.setTeams(new ArrayList<>(Arrays.asList(team6)));
    player25.setTeams(new ArrayList<>(Arrays.asList(team7)));
    player26.setTeams(new ArrayList<>(Arrays.asList(team7)));
    player27.setTeams(new ArrayList<>(Arrays.asList(team7)));
    player28.setTeams(new ArrayList<>(Arrays.asList(team7)));
    player29.setTeams(new ArrayList<>(Arrays.asList(team8)));
    player30.setTeams(new ArrayList<>(Arrays.asList(team8)));
    player31.setTeams(new ArrayList<>(Arrays.asList(team8)));
    player32.setTeams(new ArrayList<>(Arrays.asList(team8)));
    goalkeeper1.setTeams(new ArrayList<>(Arrays.asList(team1)));
    goalkeeper2.setTeams(new ArrayList<>(Arrays.asList(team2)));
    goalkeeper3.setTeams(new ArrayList<>(Arrays.asList(team3)));
    goalkeeper4.setTeams(new ArrayList<>(Arrays.asList(team4)));
    goalkeeper5.setTeams(new ArrayList<>(Arrays.asList(team5)));
    goalkeeper6.setTeams(new ArrayList<>(Arrays.asList(team6)));
    goalkeeper7.setTeams(new ArrayList<>(Arrays.asList(team7)));
    goalkeeper8.setTeams(new ArrayList<>(Arrays.asList(team8)));
    playerRepository.saveAll(
        new ArrayList<>(
            Arrays.asList(
                player1, player2, player3, player4, player5, player6, player7, player8,
                player9, player10, player11, player12, player13, player14, player15, player16,
                player17, player18, player19, player20, player21, player22, player23, player24,
                player25, player26, player27, player28, player29, player30, player31, player32,
                goalkeeper1, goalkeeper2, goalkeeper3, goalkeeper4, goalkeeper5, goalkeeper6, goalkeeper7, goalkeeper8
            )
        )
    );
  }

  private void generateGameWithStatsAndLineups() {

    game1 = new Game();

    game1.setDateTime(LocalDateTime.now());
    game1.setGameType(Game.GameType.FRIENDLY);
    game1.setLocation(new GameLocation("Estádio Nacional", "Lisboa"));
    game1.setRefereeTeam(refereeTeam1);
    game1.setStartingTeams(new ArrayList<>(Arrays.asList(startingTeam1, startingTeam2)));

    startingTeam1.addGame(game1);
    startingTeam2.addGame(game1);

    game2 = new Game();
    game2.setDateTime(LocalDateTime.now().plus(1, java.time.temporal.ChronoUnit.DAYS));
    game2.setGameType(Game.GameType.FRIENDLY);
    game2.setLocation(new GameLocation("Estádio do Porto", "Porto"));
    game2.setRefereeTeam(refereeTeam2);
    game2.setStartingTeams(new ArrayList<>(Arrays.asList(startingTeam2, startingTeam3)));

    startingTeam2.addGame(game2);
    startingTeam3.addGame(game2);

    game3 = new Game();
    game3.setDateTime(LocalDateTime.now().plus(2, java.time.temporal.ChronoUnit.DAYS));
    game3.setGameType(Game.GameType.FRIENDLY);
    game3.setLocation(new GameLocation("Estádio do Dragão", "Porto"));
    game3.setRefereeTeam(refereeTeam3);
    game3.setStartingTeams(new ArrayList<>(Arrays.asList(startingTeam1, startingTeam3)));

    startingTeam1.addGame(game3);
    startingTeam3.addGame(game3);

    startingTeamRepository.saveAll(
        new ArrayList<>(Arrays.asList(startingTeam1, startingTeam2, startingTeam3)));

    gameRepository.saveAll(new ArrayList<>(Arrays.asList(game1, game2, game3)));

    Map<Long, Integer> goalsHomeTeam = new HashMap<>();
    Map<Long, Integer> goalsAwayTeam = new HashMap<>();

    goalsHomeTeam.put(player1.getId(), 1); // player 1 marcou 1 golo - home
    goalsHomeTeam.put(player3.getId(), 1); // player 3 marcou 1 golo - home
    
    Stats stats1 = new Stats();
    stats1.setGoalsAwayTeam(goalsAwayTeam);
    stats1.setGoalsHomeTeam(goalsHomeTeam);
    
    Map<Long, Integer> goalsHomeTeam2 = new HashMap<>();
    Map<Long, Integer> goalsAwayTeam2 = new HashMap<>();
    goalsHomeTeam2.put(player6.getId(), 1); // player 6 marcou 1 golos - home

    Stats stats2 = new Stats();
    stats2.setGoalsHomeTeam(goalsHomeTeam2);
    stats2.setGoalsAwayTeam(goalsAwayTeam2);

    statsRepository.saveAll(new ArrayList<>(Arrays.asList(stats1, stats2)));

    // cartoes do game1
    Card redCard = new Card(player1, Card.CardType.RED);
    Card yellow1 = new Card(player4, Card.CardType.YELLOW);
    Card yellow2 = new Card(player4, Card.CardType.YELLOW);
    cardRepository.saveAll(new ArrayList<>(Arrays.asList(redCard, yellow1, yellow2)));

    stats1.setCards(new ArrayList<>(Arrays.asList(redCard, yellow1, yellow2)));
    statsRepository.save(stats1);

    game1.setStats(stats1);
    gameRepository.save(game1);

    // cartoes para o game2
    Card yellow3 = new Card(player6, Card.CardType.YELLOW);
    cardRepository.save(yellow3);

    stats2.setCards(new ArrayList<>(List.of(yellow3)));
    statsRepository.save(stats2);

    game2.setStats(stats2);
    gameRepository.save(game2);
  }

  private void generateChampionshipByPointss() {

    ChampionshipByPoints champ1 = new ChampionshipByPoints(
        "Liga de Portugal", 
        new ArrayList<>(Arrays.asList(team1, team2, team3, team4, team5, team6, team7, team8))
    );

    List<Team> teams = Arrays.asList(team1, team2, team3, team4, team5, team6, team7, team8);
    List<StartingTeam> startingTeams = Arrays.asList(
        startingTeam1, startingTeam2, startingTeam3, startingTeam4,
        startingTeam5, startingTeam6, startingTeam7, startingTeam8
    );

    // Define stadiums/cities for each team
    Map<Team, String> teamCities = new HashMap<>();
    teamCities.put(team1, "Porto");
    teamCities.put(team2, "Lisboa");
    teamCities.put(team3, "Lisboa");
    teamCities.put(team4, "Lisboa");
    teamCities.put(team5, "Porto");
    teamCities.put(team6, "Guimarães");
    teamCities.put(team7, "Braga");
    teamCities.put(team8, "Vila do Conde");

    List<Game> champGames = new ArrayList<>();
    List<Stats> champStats = new ArrayList<>();

    for (int i = 0; i < teams.size(); i++) {
        for (int j = i + 1; j < teams.size(); j++) {
            Game game = new Game();
            game.setDateTime(LocalDateTime.now().plusHours(24 + champGames.size()));
            game.setGameType(Game.GameType.CHAMPIONSHIP);
            String city = teamCities.getOrDefault(teams.get(i), "Portugal");
            game.setLocation(new GameLocation("Estádio do " + teams.get(i).getName(), city));
            game.setRefereeTeam(refereeTeam1);
            game.setStartingTeams(new ArrayList<>(Arrays.asList(startingTeams.get(i), startingTeams.get(j))));
            game.setChampionship(champ1);

            Stats stats = new Stats();
            statsRepository.save(stats);
            game.setStats(stats);

            champGames.add(game);
            champStats.add(stats);
        }
    }

    statsRepository.saveAll(champStats);
    champ1.setGames(champGames);
    championshipRepository.save(champ1);
    gameRepository.saveAll(champGames);
    
    ChampionshipByPoints champ2 = new ChampionshipByPoints(
        "Mini Liga", 
        new ArrayList<>(Arrays.asList(team1, team2, team3, team4, team5, team6, team7, team8))
    );
    championshipRepository.save(champ2);

  }
}
