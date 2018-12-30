import TeamRatio from "./TeamRatio";

export default class Quiz {
  quizId: number;
  name: string;
  status: number = 1 | 2 | 3;
  statusName: string;
  teamLeft: TeamRatio;
  teamRight: TeamRatio;
  result: string;

  constructor(quizId: number,
    name: string,
    status: number,
    statusName?: string,
    teamLeft?: TeamRatio,
    teamRight?: TeamRatio,
    result?: string) {
    this.quizId = quizId;
    this.name = name;
    this.status = status;
    this.statusName = statusName;
    this.teamLeft = teamLeft;
    this.teamRight = teamRight;
    this.result = result;
  }
}