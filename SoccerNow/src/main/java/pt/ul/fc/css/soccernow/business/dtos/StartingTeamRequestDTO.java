package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;

public class StartingTeamRequestDTO {
    private List<Long> players;
    private Long goalKeeper;

    /** No-argument constructor. */
    public StartingTeamRequestDTO() {}

    /**
     * Constructs a StartingTeamRequestDTO with the provided players and goalkeeper.
     *
     * @param players   the list of player IDs
     * @param goalKeeper the ID of the goalkeeper
     */
    public StartingTeamRequestDTO(List<Long> players, Long goalKeeper) {
        this.players = players;
        this.goalKeeper = goalKeeper;
    }

    // Getters and Setters
    public List<Long> getPlayers() {
        return players;
    }

    public void setPlayers(List<Long> players) {
        this.players = players;
    }

    public Long getGoalKeeper() {
        return goalKeeper;
    }

    public void setGoalKeeper(Long goalKeeper) {
        this.goalKeeper = goalKeeper;
    }
}
