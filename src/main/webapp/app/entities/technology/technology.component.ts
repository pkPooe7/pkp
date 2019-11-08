import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ITechnology } from 'app/shared/model/technology.model';
import { AccountService } from 'app/core/auth/account.service';
import { TechnologyService } from './technology.service';

@Component({
  selector: 'jhi-technology',
  templateUrl: './technology.component.html'
})
export class TechnologyComponent implements OnInit, OnDestroy {
  technologies: ITechnology[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected technologyService: TechnologyService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.technologyService
      .query()
      .pipe(
        filter((res: HttpResponse<ITechnology[]>) => res.ok),
        map((res: HttpResponse<ITechnology[]>) => res.body)
      )
      .subscribe((res: ITechnology[]) => {
        this.technologies = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTechnologies();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITechnology) {
    return item.id;
  }

  registerChangeInTechnologies() {
    this.eventSubscriber = this.eventManager.subscribe('technologyListModification', response => this.loadAll());
  }
}
