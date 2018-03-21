/*
 *  Copyright 2018 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.backend.c.generate;

import java.io.PrintWriter;
import org.teavm.model.ValueType;
import org.teavm.model.util.VariableType;

public class CodeWriter {
    private PrintWriter writer;
    private int indentLevel;
    private boolean isLineStart;

    public CodeWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public CodeWriter println() {
        return println("");
    }

    public CodeWriter println(String string) {
        addIndentIfNecessary();
        writer.print(string);
        writer.print("\n");
        isLineStart = true;
        return this;
    }

    public CodeWriter print(String string) {
        addIndentIfNecessary();
        writer.print(string);
        return this;
    }

    public CodeWriter indent() {
        indentLevel++;
        return this;
    }

    public CodeWriter outdent() {
        indentLevel--;
        return this;
    }

    private void addIndentIfNecessary() {
        if (isLineStart) {
            for (int i = 0; i < indentLevel; ++i) {
                writer.print("    ");
            }
            isLineStart = false;
        }
    }

    public CodeWriter printType(ValueType type) {
        print(typeAsString(type));
        return this;
    }

    public CodeWriter printStrictType(ValueType type) {
        print(strictTypeAsString(type));
        return this;
    }

    public static String strictTypeAsString(ValueType type) {
        if (type instanceof ValueType.Primitive) {
            switch (((ValueType.Primitive) type).getKind()) {
                case BOOLEAN:
                case BYTE:
                    return "int8_t";
                case SHORT:
                    return "int16_t";
                case CHARACTER:
                    return "char16_t";
                default:
                    break;
            }
        }
        return typeAsString(type);
    }

    public static String typeAsString(ValueType type) {
        if (type instanceof ValueType.Primitive) {
            switch (((ValueType.Primitive) type).getKind()) {
                case BOOLEAN:
                case BYTE:
                case SHORT:
                case CHARACTER:
                case INTEGER:
                    return "int32_t";
                case LONG:
                    return "int64_t";
                case FLOAT:
                    return "float";
                case DOUBLE:
                    return "double";
            }
        } else if (type instanceof ValueType.Array) {
            return "JavaArray*";
        } else if (type == ValueType.VOID) {
            return "void";
        }

        return "void*";
    }

    public CodeWriter printType(VariableType type) {
        switch (type) {
            case INT:
                print("int32_t");
                break;
            case LONG:
                print("int64_t");
                break;
            case FLOAT:
                print("float");
                break;
            case DOUBLE:
                print("double");
                break;
            case OBJECT:
                print("void*");
                break;
            case BYTE_ARRAY:
            case CHAR_ARRAY:
            case SHORT_ARRAY:
            case INT_ARRAY:
            case LONG_ARRAY:
            case FLOAT_ARRAY:
            case DOUBLE_ARRAY:
            case OBJECT_ARRAY:
                print("JavaArray*");
                break;
        }

        return this;
    }
}