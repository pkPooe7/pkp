import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { SolarSystemUpdateComponent } from 'app/entities/solar-system/solar-system-update.component';
import { SolarSystemService } from 'app/entities/solar-system/solar-system.service';
import { SolarSystem } from 'app/shared/model/solar-system.model';

describe('Component Tests', () => {
  describe('SolarSystem Management Update Component', () => {
    let comp: SolarSystemUpdateComponent;
    let fixture: ComponentFixture<SolarSystemUpdateComponent>;
    let service: SolarSystemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [SolarSystemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SolarSystemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SolarSystemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolarSystemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SolarSystem(123);
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
        const entity = new SolarSystem();
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
