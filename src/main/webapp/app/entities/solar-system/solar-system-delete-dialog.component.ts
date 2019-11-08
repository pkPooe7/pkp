import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISolarSystem } from 'app/shared/model/solar-system.model';
import { SolarSystemService } from './solar-system.service';

@Component({
  selector: 'jhi-solar-system-delete-dialog',
  templateUrl: './solar-system-delete-dialog.component.html'
})
export class SolarSystemDeleteDialogComponent {
  solarSystem: ISolarSystem;

  constructor(
    protected solarSystemService: SolarSystemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.solarSystemService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'solarSystemListModification',
        content: 'Deleted an solarSystem'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-solar-system-delete-popup',
  template: ''
})
export class SolarSystemDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ solarSystem }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SolarSystemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.solarSystem = solarSystem;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/solar-system', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/solar-system', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
