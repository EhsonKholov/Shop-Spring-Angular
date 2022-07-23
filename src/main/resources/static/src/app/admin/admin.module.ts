import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import { AdminLayoutComponent } from './components/shared/admin-layout/admin-layout.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { AddPageComponent } from './components/add-page/add-page.component';
import { DashboardPageComponent } from './components/dashboard-page/dashboard-page.component';
import { EditPageComponent } from './components/edit-page/edit-page.component';
import { OrdersPageComponent } from './components/orders-page/orders-page.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AuthGuard} from "../services/auth.guard";
import { QuillModule } from 'ngx-quill'
import {SearchPipe} from "../services/search.pipe";


const routes: Routes = [
  {path: '', component: AdminLayoutComponent, children: [
      {path: '', redirectTo: '/admin/login', pathMatch: 'full'},
      {path: 'login', component: LoginPageComponent},
      {path: 'dashboard', component: DashboardPageComponent, canActivate: [AuthGuard]},
      {path: 'add', component: AddPageComponent, canActivate: [AuthGuard]},
      {path: 'orders', component: OrdersPageComponent, canActivate: [AuthGuard]},
      {path: 'product/:id/edit', component: EditPageComponent, canActivate: [AuthGuard]}
    ]}
]

@NgModule({
  declarations: [
    AdminLayoutComponent,
    LoginPageComponent,
    AddPageComponent,
    DashboardPageComponent,
    EditPageComponent,
    OrdersPageComponent,
    SearchPipe
  ],
  exports: [
    SearchPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    QuillModule.forRoot(),
    RouterModule.forChild(routes)
  ]
})
export class AdminModule {

}
