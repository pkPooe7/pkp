import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IClassification } from 'app/shared/model/classification.model';
import { AccountService } from 'app/core/auth/account.service';
import { ClassificationService } from './classification.service';

@Component({
  selector: 'jhi-classification',
  templateUrl: './classification.component.html'
})
export class ClassificationComponent implements OnInit, OnDestroy {
  classifications: IClassification[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected classificationService: ClassificationService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.classificationService
      .query()
      .pipe(
        filter((res: HttpResponse<IClassification[]>) => res.ok),
        map((res: HttpResponse<IClassification[]>) => res.body)
      )
      .subscribe((res: IClassification[]) => {
        this.classifications = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInClassifications();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IClassification) {
    return item.id;
  }

  registerChangeInClassifications() {
    this.eventSubscriber = this.eventManager.subscribe('classificationListModification', response => this.loadAll());
  }
}
