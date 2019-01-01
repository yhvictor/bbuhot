import { Team } from './team';

export class TeamRatio extends Team {
  ratio: number;

  constructor(teamId: number, teamName: string, ratio: number) {
    super(teamId, teamName);
    this.ratio = ratio;
  }
}
