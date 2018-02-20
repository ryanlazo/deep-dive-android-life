package edu.cnm.deepdive.life;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import edu.cnm.deepdive.life.ca.Model;

public class MainActivity extends AppCompatActivity {

  private Model model;
  private boolean running = false;
  private Runner runner = null;
  private TextView generationCounter;
  private SeekBar populationDensity;
  private String generationFormat;
  private TerrainView terrainView;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    terrainView = findViewById(R.id.terrain_view);
    generationCounter = findViewById(R.id.generation_counter);
    populationDensity = findViewById(R.id.population_density);
    generationFormat = generationCounter.getText().toString();
    model = new Model(200,200);
    model.populate(populationDensity.getProgress() / 100.0);
    updateView(model.getTerrain(), model.getGeneration());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
   getMenuInflater().inflate(R.menu.options, menu);
   return true;

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {

      case R.id.action_step:
        model.advance();
        updateView(model.getTerrain(), model.getGeneration());
        break;

      case R.id.action_run:
        running = true;
        runner = new Runner ();
        runner.start();
        break;

      case R.id.action_stop:
        running = false;
        runner = null;
        break;

      case R.id.action_reset:
        model.populate(populationDensity.getProgress() / 100.0);
        updateView(model.getTerrain(), model.getGeneration());
        break;

      default:
        return super.onOptionsItemSelected(item);
    }
    invalidateOptionsMenu();
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    Log.d(getClass().getName(), "entering onPrepareOptionsMenu");
    menu.findItem(R.id.action_step).setEnabled(!running);
    menu.findItem(R.id.action_run).setVisible(!running);
    menu.findItem(R.id.action_stop).setVisible(running);
    menu.findItem(R.id.action_reset).setEnabled(!running);
    return true;
  }

  private void updateView(byte[][] terrain, final int generation) {
    terrainView.setTerrain(terrain);
    if (Looper.getMainLooper().getThread()== Thread.currentThread()) {
      terrainView.invalidate();
      generationCounter.setText(String.format(generationFormat, generation));
    } else {
      terrainView.postInvalidate();
      runOnUiThread(new Runnable() {
        @Override
          public void run() {
          generationCounter.setText(String.format(generationFormat, generation));
        }
      });
    }


  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
          model.advance();
          if (!terrainView.isUpdatePending()) {
            update();
          }

      }
      update();
    }

    private void update() {
     byte[][] terrain = model.getTerrain();
     int generation = model.getGeneration();
      updateView(terrain, generation);

    }

  }


}


