/*
 * Copyright (c) 2022. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
 
@file:JvmMultifileClass
@file:JvmName("BuiltInTypes")
@file:Suppress("ObjectPropertyName", "SpellCheckingInspection", "NewApi", "DEPRECATION")

/*
 * This file is automatically generated by [meowool-cloak](https://github.com/meowool-catnip/cloak/built-ins/generator).
 */
package com.meowool.cloak.builtin.types

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.app.CoreComponentFactory
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.meowool.cloak.Type
import com.meowool.cloak.type
import com.meowool.sweekt.LazyInit

/**
 * Represents the type of [AppCompatActivity] class.
 */
@LazyInit val _AppCompatActivity: Type<AppCompatActivity> = AppCompatActivity::class.java.type

/**
 * Represents the type of [ComponentActivity] class.
 */
@LazyInit val _ComponentActivity: Type<ComponentActivity> = ComponentActivity::class.java.type

/**
 * Represents the type of [androidx.core.app.ComponentActivity] class.
 */
@LazyInit val _CoreAppComponentActivity: Type<androidx.core.app.ComponentActivity> =
    androidx.core.app.ComponentActivity::class.java.type

/**
 * Represents the type of [CoreComponentFactory] class.
 */
@LazyInit val _CoreComponentFactory: Type<CoreComponentFactory> = CoreComponentFactory::class.java.type

/**
 * Represents the type of [ActivityResult] class.
 */
@LazyInit val _ActivityResult: Type<ActivityResult> = ActivityResult::class.java.type

/**
 * Represents the type of [AppCompatDialog] class.
 */
@LazyInit val _AppCompatDialog: Type<AppCompatDialog> = AppCompatDialog::class.java.type

/**
 * Represents the type of [AppCompatDelegate] class.
 */
@LazyInit val _AppCompatDelegate: Type<AppCompatDelegate> = AppCompatDelegate::class.java.type

/**
 * Represents the type of [AppCompatDialogFragment] class.
 */
@LazyInit val _AppCompatDialogFragment: Type<AppCompatDialogFragment> =
    AppCompatDialogFragment::class.java.type

/**
 * Represents the type of [Fragment] class.
 */
@LazyInit val _FragmentX: Type<Fragment> = Fragment::class.java.type

/**
 * Represents the type of [FragmentActivity] class.
 */
@LazyInit val _FragmentActivityX: Type<FragmentActivity> = FragmentActivity::class.java.type

/**
 * Represents the type of [DialogFragment] class.
 */
@LazyInit val _DialogFragmentX: Type<DialogFragment> = DialogFragment::class.java.type

/**
 * Represents the type of [AlertDialog] class.
 */
@LazyInit val _AlertDialogX: Type<AlertDialog> = AlertDialog::class.java.type

/**
 * Represents the type of [ActionBar] class.
 */
@LazyInit val _ActionBarX: Type<ActionBar> = ActionBar::class.java.type
