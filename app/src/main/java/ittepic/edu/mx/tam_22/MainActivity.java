package ittepic.edu.mx.tam_22;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText identificador,nombre,domicilio;
    Button insertar,consultar,actualizar,eliminar;
    BaseDatos db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        identificador=(EditText) findViewById(R.id.identificador);
        nombre=(EditText) findViewById(R.id.nombre);
        domicilio = (EditText) findViewById(R.id.domicilio);
        insertar=(Button) findViewById(R.id.btninsertar);
        consultar=(Button) findViewById(R.id.btnconsultar);
        actualizar=(Button) findViewById(R.id.btnactualizar);
        eliminar=(Button) findViewById(R.id.btneliminar);

        db= new BaseDatos(this, "prueba1",null,1);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatos();
            }
        });
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarDatos();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarDatos();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatos();
            }
        });
    }
    private void insertarDatos(){
        try{
            SQLiteDatabase base = db.getWritableDatabase();
            String sentenciaSQL = "INSERT INTO PERSONA VALUES(ID,'NOMBRE','DOMICILIO')";

            sentenciaSQL= sentenciaSQL.replace("ID",identificador.getText().toString());
            sentenciaSQL= sentenciaSQL.replace("NOMBRE",nombre.getText().toString());
            sentenciaSQL= sentenciaSQL.replace("DOMICILIO",domicilio.getText().toString());

            base.execSQL(sentenciaSQL);

            Toast.makeText(this,"SE INSRTO CON EXITO", Toast.LENGTH_LONG).show();

            identificador.setText("");nombre.setText("");domicilio.setText("");
            base.close();

        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    private void consultarDatos(){
        try {
            SQLiteDatabase base = db.getReadableDatabase();
            String sentenciaSQL = "SELECT * FROM PERSONA WHERE ID=" + identificador.getText().toString();

            Cursor resultadoConsulta = base.rawQuery(sentenciaSQL, null);
            if (resultadoConsulta.moveToFirst() == false) {
                Toast.makeText(this, "ADVERTENCIA no hay concidencias", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setTitle("Resultados").
                        setMessage("Nombre " + resultadoConsulta.getString(1) +
                                "Domicilio " + resultadoConsulta.getString(2)).
                        setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alerta.show();
            }
            base.close();
        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarDatos(){
        final EditText idBorrar=new EditText(this);
        idBorrar.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder mensaje= new AlertDialog.Builder(this);
        mensaje.setTitle("ATENCION: ").setMessage("ID a borrar: ").
                setView(idBorrar).
                setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarDatos2(idBorrar.getText().toString());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        mensaje.show();

    }

    private void eliminarDatos2(String idborrar){
        try {
            SQLiteDatabase base = db.getWritableDatabase();
            String sentenciaSQL = "DELETE FROM PERSONA WHERE ID="+idborrar;


            base.execSQL(sentenciaSQL);//aqui se hace la insecion

            Toast.makeText(this,"SE Borro el id "+idborrar, Toast.LENGTH_LONG).show();

        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarDatos(){
        final EditText idActualizar=new EditText(this);
        idActualizar.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder mensaje= new AlertDialog.Builder(this);
        mensaje.setTitle("ATENCION: ").setMessage("ID a borrar: ").
                setView(idActualizar).
                setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        actualizarDatos2(idActualizar.getText().toString());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        mensaje.show();
    }
    private void actualizarDatos2(final String idActualizar){
        try {
            SQLiteDatabase base=db.getReadableDatabase();
            String sentenciaSQL= "SELECT * FROM PRESONA WHERE ID="+idActualizar;

            Cursor respuesta = base.rawQuery(sentenciaSQL,null);
            if (respuesta.moveToFirst()==false){
                Toast.makeText(this,"Error no existe el id!!",Toast.LENGTH_LONG).show();
            }else {
                final EditText nombreActualizar, domicilioActualizar;
                nombreActualizar= new EditText(this);
                domicilioActualizar=new EditText(this);
                LinearLayout layout=new LinearLayout(this);

                nombreActualizar.setText(respuesta.getString(1));
                domicilioActualizar.setText(respuesta.getString(2));
                layout.setOrientation(LinearLayout.VERTICAL);

                layout.addView(nombreActualizar);
                layout.addView(domicilioActualizar);

                AlertDialog.Builder mensaje=new AlertDialog.Builder(this);
                mensaje.setTitle("Atencion").
                        setMessage("Modifique datos").
                        setView(layout).
                        setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                actualizarDatos3(idActualizar,nombreActualizar.getText().toString(),
                                        domicilioActualizar.getText().toString());

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                mensaje.show();
            }
            base.close();
        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarDatos3(String id,String nombre,String domicilio ){
        try{
            SQLiteDatabase base=db.getWritableDatabase();
            String SQL= "UPDATE PERSONA SET NOMBRE='XX1' , DOMICILIO='XX2' WHERE ID="+id;

            SQL=SQL.replace("XX1",nombre);
            SQL=SQL.replace("XX2",domicilio);

            base.execSQL(SQL);

            Toast.makeText(this,"Se acualizo registro con id "+id,Toast.LENGTH_LONG).show();
            base.close();

        }catch (SQLiteException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
