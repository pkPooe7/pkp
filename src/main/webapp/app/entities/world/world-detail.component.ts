import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorld } from 'app/shared/model/world.model';

@Component({
  selector: 'jhi-world-detail',
  templateUrl: './world-detail.component.html'
})
export class WorldDetailComponent implements OnInit {
  world: IWorld;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ world }) => {
      this.world = world;
    });
  }

  previousState() {
    window.history.back();
  }
}
