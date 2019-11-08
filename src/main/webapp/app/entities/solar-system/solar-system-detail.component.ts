import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISolarSystem } from 'app/shared/model/solar-system.model';

@Component({
  selector: 'jhi-solar-system-detail',
  templateUrl: './solar-system-detail.component.html'
})
export class SolarSystemDetailComponent implements OnInit {
  solarSystem: ISolarSystem;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ solarSystem }) => {
      this.solarSystem = solarSystem;
    });
  }

  previousState() {
    window.history.back();
  }
}
