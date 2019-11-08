import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReBaseTestModule } from '../../../test.module';
import { AlienDeleteDialogComponent } from 'app/entities/alien/alien-delete-dialog.component';
import { AlienService } from 'app/entities/alien/alien.service';

describe('Component Tests', () => {
  describe('Alien Management Delete Component', () => {
    let comp: AlienDeleteDialogComponent;
    let fixture: ComponentFixture<AlienDeleteDialogComponent>;
    let service: AlienService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [AlienDeleteDialogComponent]
      })
        .overrideTemplate(AlienDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlienDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlienService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
