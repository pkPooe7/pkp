import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAlien, Alien } from 'app/shared/model/alien.model';
import { AlienService } from './alien.service';
import { IClassification } from 'app/shared/model/classification.model';
import { ClassificationService } from 'app/entities/classification/classification.service';
import { IWorld } from 'app/shared/model/world.model';
import { WorldService } from 'app/entities/world/world.service';

@Component({
  selector: 'jhi-alien-update',
  templateUrl: './alien-update.component.html'
})
export class AlienUpdateComponent implements OnInit {
  isSaving: boolean;

  catergories: IClassification[];

  worlds: IWorld[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    species: [null, [Validators.required, Validators.maxLength(50)]],
    planet: [null, [Validators.required, Validators.maxLength(50)]],
    catergory: [],
    homeWorld: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected alienService: AlienService,
    protected classificationService: ClassificationService,
    protected worldService: WorldService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ alien }) => {
      this.updateForm(alien);
    });
    this.classificationService
      .query({ filter: 'type-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IClassification[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassification[]>) => response.body)
      )
      .subscribe(
        (res: IClassification[]) => {
          if (!this.editForm.get('catergory').value || !this.editForm.get('catergory').value.id) {
            this.catergories = res;
          } else {
            this.classificationService
              .find(this.editForm.get('catergory').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IClassification>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IClassification>) => subResponse.body)
              )
              .subscribe(
                (subRes: IClassification) => (this.catergories = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.worldService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IWorld[]>) => mayBeOk.ok),
        map((response: HttpResponse<IWorld[]>) => response.body)
      )
      .subscribe((res: IWorld[]) => (this.worlds = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(alien: IAlien) {
    this.editForm.patchValue({
      id: alien.id,
      name: alien.name,
      species: alien.species,
      planet: alien.planet,
      catergory: alien.catergory,
      homeWorld: alien.homeWorld
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const alien = this.createFromForm();
    if (alien.id !== undefined) {
      this.subscribeToSaveResponse(this.alienService.update(alien));
    } else {
      this.subscribeToSaveResponse(this.alienService.create(alien));
    }
  }

  private createFromForm(): IAlien {
    return {
      ...new Alien(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      species: this.editForm.get(['species']).value,
      planet: this.editForm.get(['planet']).value,
      catergory: this.editForm.get(['catergory']).value,
      homeWorld: this.editForm.get(['homeWorld']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlien>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackClassificationById(index: number, item: IClassification) {
    return item.id;
  }

  trackWorldById(index: number, item: IWorld) {
    return item.id;
  }
}
