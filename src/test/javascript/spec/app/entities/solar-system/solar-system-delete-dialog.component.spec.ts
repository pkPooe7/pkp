import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReBaseTestModule } from '../../../test.module';
import { SolarSystemDeleteDialogComponent } from 'app/entities/solar-system/solar-system-delete-dialog.component';
import { SolarSystemService } from 'app/entities/solar-system/solar-system.service';

describe('Component Tests', () => {
  describe('SolarSystem Management Delete Component', () => {
    let comp: SolarSystemDeleteDialogComponent;
    let fixture: ComponentFixture<SolarSystemDeleteDialogComponent>;
    let service: SolarSystemService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [SolarSystemDeleteDialogComponent]
      })
        .overrideTemplate(SolarSystemDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolarSystemDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolarSystemService);
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
