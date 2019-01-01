import { TeamRatio } from './team-ratio';

enum QuizStatus {
  // TODO(anyone): rename to reflect statuses.
  STATUS_DOING,
  STATUS_TODO,
  STATUS_DONE
}

export class Quiz {
  static readonly Status = QuizStatus;
  quizId: number;
  name: string;
  status: QuizStatus;
  teamLeft: TeamRatio;
  teamRight: TeamRatio;
  result: string;

  constructor(
    quizId: number,
    name: string,
    status: QuizStatus,
    teamLeft: TeamRatio,
    teamRight: TeamRatio,
    result: string
  ) {
    this.quizId = quizId;
    this.name = name;
    this.status = status;
    this.teamLeft = teamLeft;
    this.teamRight = teamRight;
    this.result = result;
  }
}
