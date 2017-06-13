package com.faza.project.fuzzylogicexample;

import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Dibuat oleh Faza Zulfika Permana Putra
 */

public class MainActivity extends AppCompatActivity {

    public Button btnSearch;
    public EditText etDemand, etStock;
    public TextInputLayout tilDemand, tilStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDemand = (EditText) findViewById(R.id.et_demand);
        etStock = (EditText) findViewById(R.id.et_stock);

        etDemand.addTextChangedListener(new DemandChangeListener());
        etStock.addTextChangedListener(new StockChangeListener());

        tilDemand = (TextInputLayout) findViewById(R.id.til_demand);
        tilStock = (TextInputLayout) findViewById(R.id.til_stock);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new SearchClickListener());

        etDemand.setText("");
        etStock.setText("");
    }

    private void checkDemandInput(String demand) {
        if (demand.equals("")) {
            tilDemand.setErrorEnabled(true);
            tilDemand.setError("Input tidak dapat kosong...");

            checkError();

            return;
        } else
            tilDemand.setErrorEnabled(false);

        Integer inputInt = Integer.parseInt(demand);

        if (inputInt < 0) {
            tilDemand.setErrorEnabled(true);
            tilDemand.setError("Input tidak dapat minus...");

            checkError();

            return;
        } else
            tilDemand.setErrorEnabled(false);

        checkError();
    }

    private void checkStockInput(String stock) {
        if (stock.equals("")) {
            tilStock.setErrorEnabled(true);
            tilStock.setError("Input tidak dapat kosong...");

            checkError();

            return;
        } else
            tilStock.setErrorEnabled(false);

        Integer inputInt = Integer.parseInt(stock);

        if (inputInt < 0) {
            tilStock.setErrorEnabled(true);
            tilStock.setError("Input tidak dapat minus...");

            checkError();

            return;
        } else
            tilStock.setErrorEnabled(false);

        checkError();
    }

    private void checkError() {
        boolean demand = tilDemand.isErrorEnabled();
        boolean stock = tilStock.isErrorEnabled();

        int color;

        if (demand || stock) {
            color = ContextCompat.getColor(this, R.color.colorGray);
            btnSearch.setEnabled(false);
        } else {
            color = ContextCompat.getColor(this, R.color.colorPrimary);
            btnSearch.setEnabled(true);
        }

        btnSearch.setBackgroundColor(color);
    }

    private void setMembership(Integer demand, Integer stock) {
        ArrayList<FuzzySet> demandSets = FuzzyLogicExample.demandSets;
        ArrayList<FuzzySet> stockSets = FuzzyLogicExample.stockSets;

        calculateMembership(demandSets, demand);
        calculateMembership(stockSets, stock);

        ArrayList<FuzzySet> productSets = caseChecking(demandSets, stockSets);
        Double crisp = calculateCrips(productSets);

//        Log.d("Crisp", "Produksinya adalah " + crisp);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Result");
        builder.setMessage("Jumlah kemasan yang harus diproduksi adalah " + crisp.intValue() + " kemasan");
        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogClickListener());

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void calculateMembership(ArrayList<FuzzySet> fuzzySets, Integer fuzzyNum) {
        Boolean isSet;

        for (FuzzySet fuzzySet : fuzzySets) {
            if (fuzzySet.getMax() < 0)
                isSet = fuzzyNum > fuzzySet.getMin();
            else
                isSet = fuzzyNum >= fuzzySet.getMin() && fuzzyNum <= fuzzySet.getMax();

            if (isSet) {
                switch (fuzzySet.getType()) {
                    case 1 :
                        fuzzySet.setMembership((double) 1);
                        break;
                    case 2 :
                        fuzzySet.setMembership((double) (fuzzyNum - fuzzySet.getMin()) / (double) (fuzzySet.getMax() - fuzzySet.getMin()));
                        break;
                    case 3 :
                        fuzzySet.setMembership((double) (fuzzySet.getMax() - fuzzyNum) / (double) (fuzzySet.getMax() - fuzzySet.getMin()));
                        break;
                }

//                Log.d("Membership " + fuzzySet.getName(), "Membershipnya " + fuzzySet.getMembership());
            }

            fuzzySet.setTrue(isSet);
        }
    }

    private ArrayList<FuzzySet> caseChecking(ArrayList<FuzzySet> demandSets, ArrayList<FuzzySet> stockSets) {
        ArrayList<FuzzySet> productSets = new ArrayList<>();

        for (FuzzySet demandSet : demandSets) {
            for (FuzzySet stockSet : stockSets) {
                if (demandSet.getTrue() && stockSet.getTrue()) {
                    FuzzySet productSet = null;

                    if (demandSet.getName().equals("Demand Decrease") && stockSet.getName().equals("Stock High")) {
                        productSet = new FuzzySet(7000, 2000, 3, "Production Decrease");
                        productSet.setTrue(true);
                        productSet.setMembership(Math.min(demandSet.getMembership(), stockSet.getMembership()));
                    } else if (demandSet.getName().equals("Demand Decrease") && stockSet.getName().equals("Stock Low")) {
                        productSet = new FuzzySet(7000, 2000, 3, "Production Decrease");
                        productSet.setTrue(true);
                        productSet.setMembership(Math.min(demandSet.getMembership(), stockSet.getMembership()));
                    } else if (demandSet.getName().equals("Demand Increase") && stockSet.getName().equals("Stock High")) {
                        productSet = new FuzzySet(7000, 2000, 2, "Production Increase");
                        productSet.setTrue(true);
                        productSet.setMembership(Math.min(demandSet.getMembership(), stockSet.getMembership()));
                    } else if (demandSet.getName().equals("Demand Increase") && stockSet.getName().equals("Stock Low")) {
                        productSet = new FuzzySet(7000, 2000, 2, "Production Increase");
                        productSet.setTrue(true);
                        productSet.setMembership(Math.min(demandSet.getMembership(), stockSet.getMembership()));
                    }

//                    if (productSet != null)
//                        Log.d("Membership " + productSet.getName(), "Membershipnya " + productSet.getMembership());

                    productSets.add(productSet);
                }
            }
        }

        return productSets;
    }

    private double calculateCrips(ArrayList<FuzzySet> productSets) {
        Double up = 0.0;
        Double down = 0.0;

        for (FuzzySet productSet : productSets) {
            if (productSet != null) {
                Double crisp = 0.0;

                switch (productSet.getType()) {
                    case 2 :
                        crisp = (productSet.getMembership() * (productSet.getMax() - productSet.getMin())) + productSet.getMin();
                        break;
                    case 3 :
                        crisp = productSet.getMax() - (productSet.getMembership() * (productSet.getMax() - productSet.getMin()));
                        break;
                }

//                Log.d("Crisp " + productSet.getName(), "Crisp " + crisp);

                down += productSet.getMembership();
                up += (productSet.getMembership() * crisp);
            }
        }

        return (up / down);
    }

    private class DemandChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String demand = s.toString();
            checkDemandInput(demand);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Do nothing
        }
    }

    private class StockChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String stock = s.toString();
            checkStockInput(stock);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Do nothing
        }
    }

    private class SearchClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String demand = etDemand.getText().toString();
            String stock = etStock.getText().toString();

            if (!tilDemand.isErrorEnabled() && !tilStock.isErrorEnabled()) {
                Integer demandInt = Integer.parseInt(demand);
                Integer stockInt = Integer.parseInt(stock);

                setMembership(demandInt, stockInt);
            }
        }
    }

    private class DialogClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            etDemand.setText("");
            etStock.setText("");

            dialog.dismiss();
        }
    }
}
