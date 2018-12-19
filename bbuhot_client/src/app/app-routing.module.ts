import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './homepage/homepage.component';
import {CompetitionComponent} from './competition/competition.component';


const routes: Routes = [
    {path: '', component: HomepageComponent},
    {path: '#competition', component: CompetitionComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
