import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReBaseTestModule } from '../../../test.module';
import { SolarSystemComponent } from 'app/entities/solar-system/solar-system.component';
import { SolarSystemService } from 'app/entities/solar-system/solar-system.service';
import { SolarSystem } from 'app/shared/model/solar-system.model';

describe('Component Tests', () => {
  describe('SolarSystem Management Component', () => {
    let comp: SolarSystemComponent;
    let fixture: ComponentFixture<SolarSystemComponent>;
    let service: SolarSystemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [SolarSystemComponent],
        providers: []
      })
        .overrideTemplate(SolarSystemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SolarSystemComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolarSystemService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SolarSystem(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.solarSystems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
