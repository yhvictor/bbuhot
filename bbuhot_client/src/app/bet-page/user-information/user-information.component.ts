import { Component, OnInit } from '@angular/core';
import { UserInformationHelper } from 'src/app/api/user-info-helper';

@Component({
  selector: 'bbuhot-user-information',
  templateUrl: './user-information.component.html',
  styleUrls: ['./user-information.component.scss']
})
export class UserInformationComponent implements OnInit {
  public uid: number;
  public name: string;

  constructor(private userInformationHelper: UserInformationHelper) {}

  async ngOnInit() {
    const user = await this.userInformationHelper.getUserInformationOrFetch();
    this.uid = user.getUid() as number;
    this.name = user.getName() as string;
  }
}
