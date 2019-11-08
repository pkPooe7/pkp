import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { AlienUpdateComponent } from 'app/entities/alien/alien-update.component';
import { AlienService } from 'app/entities/alien/alien.service';
import { Alien } from 'app/shared/model/alien.model';

describe('Component Tests', () => {
  describe('Alien Management Update Component', () => {
    let comp: AlienUpdateComponent;
    let fixture: ComponentFixture<AlienUpdateComponent>;
    let service: AlienService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [AlienUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AlienUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlienUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlienService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Alien(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Alien();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
