import Team from "./Team";

export default class TeamRatio extends Team {
  ratio: number;

  constructor(
    teamId: number,
    teamName: string,
    ratio: number) {
    super(teamId, teamName);
    this.ratio = ratio;

  }
}