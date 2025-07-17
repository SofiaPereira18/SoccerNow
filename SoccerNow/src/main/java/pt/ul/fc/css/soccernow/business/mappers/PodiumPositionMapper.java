package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.PodiumPositionDTO;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.handlers.ChampionshipHandler;

public class PodiumPositionMapper {

  private static ChampionshipHandler championshipHandler;

  public static PodiumPositionDTO toDTO(PodiumPosition podiumPosition) {
    if (podiumPosition == null) {
      return null;
    }

    String pos = "-";
    if (podiumPosition.getPos() != null) {
      if (podiumPosition.getPos().equals(PodiumPosition.Position.FIRST)) {
        pos = "First";
      } else if (podiumPosition.getPos().equals(PodiumPosition.Position.SECOND)) {
        pos = "Second";
      } else if (podiumPosition.getPos().equals(PodiumPosition.Position.THIRD)) {
        pos = "Third";
      }
    }

    return new PodiumPositionDTO(
        podiumPosition.getId(),
        pos,
        podiumPosition.getTeam().getId(),
        podiumPosition.getChampionship().getId(),
        podiumPosition.getPoints());
  }

  public static PodiumPosition toEntity(PodiumPositionDTO podiumPositionDTO) {
    if (podiumPositionDTO == null) {
      return null;
    }
    return championshipHandler.getPodiumPositionById(podiumPositionDTO.getId());
  }
}
