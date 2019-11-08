import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITechnology } from 'app/shared/model/technology.model';

@Component({
  selector: 'jhi-technology-detail',
  templateUrl: './technology-detail.component.html'
})
export class TechnologyDetailComponent implements OnInit {
  technology: ITechnology;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ technology }) => {
      this.technology = technology;
    });
  }

  previousState() {
    window.history.back();
  }
}
