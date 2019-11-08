import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IClassification, Classification } from 'app/shared/model/classification.model';
import { ClassificationService } from './classification.service';
import { IAlien } from 'app/shared/model/alien.model';
import { AlienService } from 'app/entities/alien/alien.service';
import { ITechnology } from 'app/shared/model/technology.model';
import { TechnologyService } from 'app/entities/technology/technology.service';

@Component({
  selector: 'jhi-classification-update',
  templateUrl: './classification-update.component.html'
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving: boolean;

  aliens: IAlien[];

  technologies: ITechnology[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    handed: [null, [Validators.required]]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected classificationService: ClassificationService,
    protected alienService: AlienService,
    protected technologyService: TechnologyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ classification }) => {
      this.updateForm(classification);
    });
    this.alienService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAlien[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAlien[]>) => response.body)
      )
      .subscribe((res: IAlien[]) => (this.aliens = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.technologyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITechnology[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITechnology[]>) => response.body)
      )
      .subscribe((res: ITechnology[]) => (this.technologies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(classification: IClassification) {
    this.editForm.patchValue({
      id: classification.id,
      name: classification.name,
      handed: classification.handed
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const classification = this.createFromForm();
    if (classification.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationService.update(classification));
    } else {
      this.subscribeToSaveResponse(this.classificationService.create(classification));
    }
  }

  private createFromForm(): IClassification {
    return {
      ...new Classification(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      handed: this.editForm.get(['handed']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>) {
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

  trackAlienById(index: number, item: IAlien) {
    return item.id;
  }

  trackTechnologyById(index: number, item: ITechnology) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
