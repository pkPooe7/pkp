import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlien } from 'app/shared/model/alien.model';

@Component({
  selector: 'jhi-alien-detail',
  templateUrl: './alien-detail.component.html'
})
export class AlienDetailComponent implements OnInit {
  alien: IAlien;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ alien }) => {
      this.alien = alien;
    });
  }

  previousState() {
    window.history.back();
  }
}
