package com.mobileapp.laba1;

import android.content.Context;
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

public class TwoMatrix extends Fragment {
    private EditText editTextMatrix;
    private EditText editTextMatrix2;
    private Button buttonSumm;
    private Button buttonSubstraction;
    private Button buttonMultiply;
    private Button saveTwoMatrixBtn;
    private Button openTwoMatrixBtn;
    private TextView textViewResult;
    double m[][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_matrix, container, false);

        editTextMatrix = view.findViewById(R.id.editTextMatrix);
        editTextMatrix2 = view.findViewById(R.id.editTextMatrix2);
        buttonSumm = view.findViewById(R.id.buttonSumm);
        buttonMultiply = view.findViewById(R.id.buttonMultiply);
        buttonSubstraction = view.findViewById(R.id.buttonSubstraction);
        textViewResult = view.findViewById(R.id.textViewResult);
        saveTwoMatrixBtn = view.findViewById(R.id.saveTwoMatrixButton);
        openTwoMatrixBtn = view.findViewById(R.id.openTwoMatrixButton);

        buttonSumm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSumm();
            }
        });

        buttonSubstraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSubstract();
            }
        });

        buttonMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMultiply();
            }
        });
        saveTwoMatrixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTwoMatrix();
            }
        });
        openTwoMatrixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwoMatrix();
            }
        });
        return view;
    }

    public void saveTwoMatrix(){
        String matrixText = editTextMatrix.getText().toString();
        String matrixText2 = editTextMatrix2.getText().toString();

        try {
            FileOutputStream fos = requireContext().openFileOutput("matrix2.txt", Context.MODE_PRIVATE);
            fos.write(matrixText.getBytes());
            fos.write("\n".getBytes());
            fos.write("\n".getBytes());
            fos.write(matrixText2.getBytes());
            Toast.makeText(requireContext(),"Data saved successfully", Toast.LENGTH_LONG).show();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openTwoMatrix(){
        try {
            FileInputStream fis = requireContext().openFileInput("matrix2.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder matrixText1 = new StringBuilder();
            StringBuilder matrixText2 = new StringBuilder();
            String line;
            boolean isSecondMatrix = false; // Флаг, що вказує на другу матрицю.

            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    isSecondMatrix = true;
                    continue;
                }
                if (isSecondMatrix) {
                    matrixText2.append(line).append("\n");
                } else {
                    matrixText1.append(line).append("\n");
                }
            }

            editTextMatrix.setText(matrixText1.toString());
            editTextMatrix2.setText(matrixText2.toString());
            Toast.makeText(requireContext(),"Data loaded successfully", Toast.LENGTH_LONG).show();
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double[][] stringToMatrix(String str){
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
                matrixStr.append(matrix[i][j]);
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

    private void calculateSumm() {
        String str = editTextMatrix.getText().toString();
        double[][] matrix1 = stringToMatrix(str);

        String str2 = editTextMatrix2.getText().toString();
        double[][] matrix2 = stringToMatrix(str2);

        double[][] summArray = summ(matrix1, matrix2);
        String summStr = matrixToString(summArray);

        textViewResult.setText("Сума матриць: \n" + summStr);
    }

    private void calculateSubstract() {
        String str = editTextMatrix.getText().toString();
        double [][] matrix1 = stringToMatrix(str);

        String str2 = editTextMatrix2.getText().toString();
        double [][] matrix2 = stringToMatrix(str2);

        double[][] substractArray = substract(matrix1, matrix2);
        String substractStr = matrixToString(substractArray);

        textViewResult.setText("Різниця матриць: \n" + substractStr);
    }

    private void calculateMultiply() {
        String str = editTextMatrix.getText().toString();
        double [][] matrix1 = stringToMatrix(str);

        String str2 = editTextMatrix2.getText().toString();
        double [][] matrix2 = stringToMatrix(str2);

        double[][] multiplyArray = multiply(matrix1, matrix2);
        String multiplyStr = matrixToString(multiplyArray);

        textViewResult.setText("Добуток матриць: \n" + multiplyStr);
    }

    private double[][] summ (double[][] matrix1, double[][] matrix2){
        double[][] resultMatix = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                resultMatix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return resultMatix;
    }

    private double[][] substract (double[][] matrix1, double[][] matrix2){
        double[][] resultMatix = new double[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                resultMatix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return resultMatix;
    }

    private static double[][] multiply (double[][] matrix1, double[][] matrix2){
        int rowsMatrix1 = matrix1. length;
        int columnsMatrix2 = matrix2.length;
        int columnsMatrix1RowsMatrix2 = matrix1.length;
        double[][] productMatrix  = new double[rowsMatrix1][columnsMatrix2];
        for (int i = 0; i < rowsMatrix1; i++) {
            for (int j = 0; j < columnsMatrix2; j++) {
                for (int k = 0; k < columnsMatrix1RowsMatrix2; k++) {
                    productMatrix[i][j]= productMatrix[i][j] + matrix1[i][k] *matrix2[k][j];
                }
            }
        }
        return productMatrix;
    }
}