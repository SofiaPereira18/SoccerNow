package pt.ul.fc.css.soccernow.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.soccernow.business.dtos.UserDetailsDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;
import pt.ul.fc.css.soccernow.business.mappers.RefereeTeamMapper;


@Service
public class UserService {
    
    @Autowired private UserHandler userHandler;

    /**
     * Retrieves a player by their ID.
     *
     * @param id the ID of the player to retrieve
     * @return Optional containing the player DTO if found, or empty Optional otherwise
     */
    public Player getPlayerById(Long id) {
        return userHandler.getPlayerById(id);
    }


    /**
     * Checks if login is valid.
     *
     * @param username the username of the user trying to login
     * @param password the password of the user trying to login
     * @return true if login is valid, false otherwise
     */
    public boolean login(String username, String password) {
        List<String> emails = new ArrayList<>();
        emails.add("fc56352@alunos.fc.ul.pt");
        emails.add("fc56913@alunos.fc.ul.pt");
        emails.add("fc56957@alunos.fc.ul.pt");

        return emails.contains(username);
    }

    /**
     * Filters the players with the given parameters.
     *
     * @param firstName the first name of the player
     * @param lastName the last name of the player
     * @param position the position of the player
     * @param goals the amount of goals scored
     * @param cards the amount of cards gaven
     * @param games the amount of games the referee was in
     */
    public List<UserDetailsDTO> filterPlayers(
        String firstName,
        String lastName,
        String position,
        Integer goals,
        Integer cards,
        Integer games) {
        List<Player> players = userHandler.getAllPlayersEntity();
        List<UserDetailsDTO> filteredList = new ArrayList<>();

        for (Player player : players) {
            // se todos os filtros forem null ou vazios, devolve a lista completa
            if ((firstName == null || firstName.trim().isEmpty())
                && (lastName == null || lastName.trim().isEmpty())
                && (position == null || position.trim().isEmpty())
                && goals == null
                && cards == null
                && games == null) {

                double[] results = userHandler.calculateTotalGamesAndGoals(player);
                int totalGames = (int) results[0];
                int totalGoals = (int) results[1];

                int totalCards = userHandler.calculateNumberCards(player);

                filteredList.add(
                    new UserDetailsDTO(
                        player.getId(),
                        player.getFirstName(),
                        player.getLastName(),
                        null,
                        player.getPosition(),
                        null,
                        totalGoals,
                        totalCards,
                        totalGames,
                        false,
                        null));

            } else {
                if (firstName != null
                    && !player.getFirstName().toLowerCase().contains(firstName.trim().toLowerCase()))
                continue;
                if (lastName != null
                    && !player.getLastName().toLowerCase().contains(lastName.trim().toLowerCase()))
                continue;

                if (position != null && !position.isEmpty()) {
                if (position.equalsIgnoreCase("OTHER") && player.getPosition() != PlayerPosition.OTHER)
                    continue;
                else if (position.equalsIgnoreCase("GOALKEEPER")
                    && player.getPosition() != PlayerPosition.GOALKEEPER) continue;
                else if (!player.getPosition().toString().equalsIgnoreCase(position)) continue;
                }

                double[] results = userHandler.calculateTotalGamesAndGoals(player);
                int totalGames = (int) results[0];
                int totalGoals = (int) results[1];

                int totalCards = userHandler.calculateNumberCards(player);

                // ver se tem o mm numero de goals, cards ou games
                if (games != null && totalGames != games) continue;
                if (goals != null && totalGoals != goals) continue;
                if (cards != null && totalCards != cards) continue;

                filteredList.add(
                    new UserDetailsDTO(
                        player.getId(),
                        player.getFirstName(),
                        player.getLastName(),
                        null,
                        player.getPosition(),
                        null,
                        totalGoals,
                        totalCards,
                        totalGames,
                        false,
                        null));
            }
        }
        return filteredList;
    }

    /**
     * Filters the referees with the given parameters.
     *
     * @param firstName the first name of the referee
     * @param lastName the last name of the referee
     * @param cards the amount of cards gaven
     * @param games the amount of games the referee was in
     */
    public List<UserDetailsDTO> filterReferees(
        String firstName, String lastName, Integer cards, Integer games) {
        List<Referee> referees = userHandler.getAllRefs();
        List<UserDetailsDTO> filteredList = new ArrayList<>();

        for (Referee referee : referees) {
            // se todos os filtros forem null ou vazios, devolve a lista completa
            if ((firstName == null || firstName.trim().isEmpty())
                && (lastName == null || lastName.trim().isEmpty())
                && cards == null
                && games == null) {

                int totalCards = userHandler.calculateGivenCards(referee);
                int totalGames = userHandler.calculateRefereeGames(referee);

                filteredList.add(
                    new UserDetailsDTO(
                        referee.getId(),
                        referee.getFirstName(),
                        referee.getLastName(),
                        null,
                        null,
                        null,
                        0,
                        totalCards,
                        totalGames,
                        referee.hasCertificate(),
                        referee.getRefereeTeams().stream()
                            .map(refTeam -> RefereeTeamMapper.toDTO(refTeam))
                            .collect(Collectors.toList())));

            } else {
                if (firstName != null
                    && !referee.getFirstName().toLowerCase().contains(firstName.trim().toLowerCase()))
                continue;
                if (lastName != null
                    && !referee.getLastName().toLowerCase().contains(lastName.trim().toLowerCase()))
                continue;

                int totalCards = userHandler.calculateGivenCards(referee);
                int totalGames = userHandler.calculateRefereeGames(referee);

                if (games != null && totalGames != games) continue;
                if (cards != null && totalCards != cards) continue;

                filteredList.add(
                    new UserDetailsDTO(
                        referee.getId(),
                        referee.getFirstName(),
                        referee.getLastName(),
                        null,
                        null,
                        null,
                        0,
                        totalCards,
                        totalGames,
                        referee.hasCertificate(),
                        referee.getRefereeTeams().stream()
                            .map(refTeam -> RefereeTeamMapper.toDTO(refTeam))
                            .collect(Collectors.toList())));
            }
        }

        return filteredList;
    }

}