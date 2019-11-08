import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { SolarSystemDetailComponent } from 'app/entities/solar-system/solar-system-detail.component';
import { SolarSystem } from 'app/shared/model/solar-system.model';

describe('Component Tests', () => {
  describe('SolarSystem Management Detail Component', () => {
    let comp: SolarSystemDetailComponent;
    let fixture: ComponentFixture<SolarSystemDetailComponent>;
    const route = ({ data: of({ solarSystem: new SolarSystem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [SolarSystemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SolarSystemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolarSystemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.solarSystem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
