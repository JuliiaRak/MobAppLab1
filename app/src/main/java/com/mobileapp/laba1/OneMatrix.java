package com.mobileapp.laba1;

import android.content.Context;
import android.opengl.Matrix;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneMatrix extends Fragment {
    private static EditText matrixInput;
    private Button calculateDeterminantButton;
    private Button calculateInverseButton;
    private Button saveOneMatrixBtn;
    private Button openOneMatrixBtn;
    private TextView resultTextView;
    double m[][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_matrix, container, false);

        matrixInput = view.findViewById(R.id.editTextMatrix);
        calculateDeterminantButton = view.findViewById(R.id.buttonCalculateDeterminant);
        calculateInverseButton = view.findViewById(R.id.buttonCalculateInverse);
        resultTextView = view.findViewById(R.id.textViewResult);
        saveOneMatrixBtn = view.findViewById(R.id.saveOneMatrixButton);
        openOneMatrixBtn = view.findViewById(R.id.openOneMatrixButton);

        calculateDeterminantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDeterminant();
            }
        });
        calculateInverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateInverse();
            }
        });
        saveOneMatrixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOneMatrix();
            }
        });
        openOneMatrixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOneMatrix();
            }
        });
        return view;
    }

    public void saveOneMatrix(){
        String matrixText = matrixInput.getText().toString();

        try {
            FileOutputStream fos = requireContext().openFileOutput("matrix.txt", Context.MODE_PRIVATE);
            fos.write(matrixText.getBytes());
            Toast.makeText(requireContext(),"Data saved successfully", Toast.LENGTH_LONG).show();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openOneMatrix(){
        try {
            FileInputStream fis = requireContext().openFileInput("matrix.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder matrixText = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                matrixText.append(line);
            }
            matrixInput.setText(matrixText.toString());
            Toast.makeText(requireContext(),"Data loaded successfully", Toast.LENGTH_LONG).show();
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double[][] stringToMatrix(String str){
        str = str.trim().replaceAll("\\s", "");
        str = str.substring(2, str.length() - 2);
        String[] subArrays = str.split("\\},\\{");
        double[][] result = new double[subArrays.length][];

        for (int i = 0; i < subArrays.length; i++) {
            String[] elements = subArrays[i].split(",");
            result[i] = new double[elements.length];
            for (int j = 0; j < elements.length; j++) {
                result[i][j] = Integer.parseInt(elements[j].trim());
            }
        }
        return result;
    }

    private String matrixToString(double[][] matrix) {
        StringBuilder matrixStr = new StringBuilder();
        matrixStr.append("{");

        for (int i = 0; i < matrix.length; i++) {
            matrixStr.append("{");
            for (int j = 0; j < matrix[i].length; j++) {
                matrixStr.append(String.format("%.2f", matrix[i][j]));
                if (j < matrix[i].length - 1) {
                    matrixStr.append(", ");
                }
            }
            matrixStr.append("}");
            if (i < matrix.length - 1) {
                matrixStr.append(",\n");
            }
        }
        matrixStr.append("}");
        return matrixStr.toString();
    }

    private void calculateDeterminant() {
        String str = matrixInput.getText().toString();
        double [][] result = stringToMatrix(str);

        double determinant = determinant(result);
        resultTextView.setText("Визначник: " + determinant);
    }

    private void calculateInverse() {
        String str = matrixInput.getText().toString();
        double [][] result = stringToMatrix(str);

        double[][] inverse = inverse(result);
        String inverseStr = matrixToString(inverse);

        resultTextView.setText("Обернена матриця: \n" + inverseStr);
    }

    private static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(submatrix(matrix, 0, i));
        return det;
    }

    private static double[][] inverse(double[][] matrix) {
        double[][] inverse = new double[matrix.length][matrix.length];

        // minors and cofactors
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                inverse[i][j] = Math.pow(-1, i + j)
                        * determinant(submatrix(matrix, i, j));

        // adjugate and determinant
        double det = 1.0 / determinant(matrix);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                double temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }

        return inverse;
    }

    private static double[][] submatrix(double[][] matrix, int row, int column) {
        double[][] submatrix = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    submatrix[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return submatrix;
    }

    public static double[][] returnMatrix(){
        String str = matrixInput.getText().toString();
        double[][] result = stringToMatrix(str);
        return result;
    }
}