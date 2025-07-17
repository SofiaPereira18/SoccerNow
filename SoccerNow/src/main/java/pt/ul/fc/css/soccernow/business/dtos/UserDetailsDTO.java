package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;

import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;

/**
 * Represents an UserDetailsDTO. 
 * Not an entity; used to return detailed user information in the verifyUser endpoint.
 * It includes both common attributes and specific ones for players and referees.
 *  
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
public class UserDetailsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String typeUser;    // pode ser referee ou player

    
    // atributos de player
    private PlayerPosition position;
    private List<TeamDTO> teams;
    private int totalGoals;
    private int totalCards;
    private int totalMatches;

    // atributos de referee
    private boolean hasCertificate;
    private List<RefereeTeamDTO> refereeTeams;


    /** No-argument constructor. */
    public UserDetailsDTO() {}


    /**
     * Constructs a UserDetailsDTO with the provided details for either a player or a referee.
     *
     * @param id                the id of the user
     * @param firstName         the first name of the user
     * @param lastName          the last name of the user
     * @param typeUser          the type of user: "PLAYER" or "REFEREE"
     * @param position          the position of the player on the field (only applicable to players)
     * @param teams             the list of teams the player belongs to (only applicable to players)
     * @param totalGoals        the total number of goals scored by the player (only applicable to players)
     * @param totalCards        the total number of cards received by the player (only applicable to players)
     * @param totalMatches      the total number of matches played or refereed by the user
     * @param hasCertificate    a flag indicating if the referee is certified (only applicable to referees)
     * @param refereeTeams      the list of teams the referee is assigned to (only applicable to referees)
     */
    public UserDetailsDTO(Long id, 
                        String firstName, 
                        String lastName, 
                        String typeUser, 
                        PlayerPosition position, 
                        List<TeamDTO> teams, 
                        int totalGoals, 
                        int totalCards, 
                        int totalMatches, 
                        boolean hasCertificate, 
                        List<RefereeTeamDTO> refereeTeams) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teams = teams;
        this.typeUser = typeUser;
        this.position = position;
        this.totalGoals = totalGoals;
        this.totalCards = totalCards;
        this.totalMatches = totalMatches;
        this.hasCertificate = hasCertificate;
        this.refereeTeams = refereeTeams;
    }


    // getters 
    
    /**
     * Returns the ID of the referee team.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the first name of the user.
     * 
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user.
     * 
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the user type: "PLAYER" or "REFEREE".
     * 
     * @return the type of the user
     */
    public String getTypeUser() {
        return typeUser;
    }

    /**
     * Returns the player's position on the field.
     * 
     * @return the player position
     */
    public PlayerPosition getPosition() {
        return position;
    }

    /**
     * Returns the list of teams the player belongs to.
     * 
     * @return the list of teams
     */
    public List<TeamDTO> getTeams() {
        return teams;
    }

    /**
     * Returns the total number of goals scored by the player.
     * 
     * @return the total goals
     */
    public int getTotalGoals() {
        return totalGoals;
    }

    /**
     * Returns the total number of cards received by the player.
     * 
     * @return the total cards
     */
    public int getTotalCards() {
        return totalCards;
    }

    /**
     * Returns the total number of matches played or refereed.
     * 
     * @return the total matches
     */
    public int getTotalMatches() {
        return totalMatches;
    }

    /**
     * Indicates whether the referee has a valid certification.
     * 
     * @return true if certified, false otherwise
     */
    public boolean isHasCertificate() {
        return hasCertificate;
    }

    /**
     * Returns the list of referee team assignments.
     * 
     * @return the list of referee teams
     */
    public List<RefereeTeamDTO> getRefereeTeams() {
        return refereeTeams;
    }

    // setters 

    /**
     * Sets the user ID.
     * 
     * @param id the user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the user type: PLAYER or REFEREE.
     * 
     * @param typeUser the user type
     */
    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    /**
     * Sets the player's field position.
     * 
     * @param position the player position
     */
    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    /**
     * Sets the list of teams the player belongs to.
     * 
     * @param teams the list of teams
     */
    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    /**
     * Sets the total goals scored by the player.
     * 
     * @param totalGoals the total goals
     */
    public void setTotalGoals(int totalGoals) {
        this.totalGoals = totalGoals;
    }

    /**
     * Sets the total number of cards received.
     * 
     * @param totalCards the total cards
     */
    public void setTotalCards(int totalCards) {
        this.totalCards = totalCards;
    }

    /**
     * Sets the total number of matches played or refereed.
     * 
     * @param totalMatches the total matches
     */
    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    /**
     * Sets whether the referee is certified.
     * 
     * @param hasCertificate true if certified, false otherwise
     */
    public void setHasCertificate(boolean hasCertificate) {
        this.hasCertificate = hasCertificate;
    }

    /**
     * Sets the list of referee teams.
     * 
     * @param refereeTeams the list of referee teams
     */
    public void setRefereeTeams(List<RefereeTeamDTO> refereeTeams) {
        this.refereeTeams = refereeTeams;
    }

}