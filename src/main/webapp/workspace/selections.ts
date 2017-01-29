/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Dana-Farber Cancer Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Component, Input, Injectable } from '@angular/core';

@Component({
  selector: 'selection',
  template: '<div>{{name}}:{{keys.length}}</div>',
  styles: ['{background-color:{{color}}}']
})
export class Selection {
  @Input() name: string;
  @Input() color: string;
  @Input() keys: string[];
}

export class Dimension {
  private current: string[] = [];
  public saved = {};

  private _selected = new BehaviorSubject<string[]>([]);
  private _changed = new BehaviorSubject<{}>({});
  public selected = this._selected.asObservable();
  public changed = this._changed.asObservable();

  select(keys: string[]): void {
    this.current = keys;
    this._selected.next(keys);
  }

  save(name: string, color: string): void {
    var s = { color: color, keys: this.current };
    this.saved[name] = s;
    this._changed.next(this.saved);
    this.select([]);
  }

  discard(name: string): void {
    if (this.saved[name]) {
      delete this.saved[name];
      this._changed.next(this.saved);
    }
  }
}

@Component({
  selector: 'row',
  template: `
    <selection *ngFor="let s of saved | keys"
      [name]="s"
      [color]="saved[s].color"
      [keys]="saved[s].keys">
    </selection>`
})
export class Row extends Dimension { }

@Component({
  selector: 'column',
  template: `
    <selection *ngFor="let s of saved | keys"
      [name]="s"
      [color]="saved[s].color"
      [keys]="saved[s].keys">
    </selection>`
})
export class Column extends Dimension { }

@Component({
  selector: 'selections',
  template: '<row></row><column></column>'
})
export class Selections { }

