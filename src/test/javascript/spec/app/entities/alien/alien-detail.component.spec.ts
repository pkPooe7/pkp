import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { AlienDetailComponent } from 'app/entities/alien/alien-detail.component';
import { Alien } from 'app/shared/model/alien.model';

describe('Component Tests', () => {
  describe('Alien Management Detail Component', () => {
    let comp: AlienDetailComponent;
    let fixture: ComponentFixture<AlienDetailComponent>;
    const route = ({ data: of({ alien: new Alien(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [AlienDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AlienDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlienDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.alien).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
