package com.algaworks.algafood.infrastructure.service.report;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@Service
public class PdfVendaReportService implements VendaReportService {

    @Autowired
    private VendaQueryService vendaQueryService;

    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
        try {

            /**
             * Pegando o fluxo de dados de um arquivo do nosso projeto
             * this.getClass() -> pega as classe do nosso projeto
             */
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

            /**
             * Vamos passar os parametros do relatorio que e o segundo campo da funcao fillReport
             * Que vai ser um HashMap<> Nesse caso podemos colocar qualquer tipo de informacao
             * REPORT_LOCALE -> vamos adicionar o locale por causa da MOEDA que queremos BR
             **/
            var parametros = new HashMap<String, Object>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));


            /**
             * Vamos criar uma collection de vendas diaria para passar para o JRBeanCollectionDataSource
             * Entao vamos criar uma collection de vendas diarias, e temos que chamar o metodo
             *consultarVendasDiarias  da classe VendaQueryServiceImpl
             * Temos aqui nossa lista de venas diaria
             */
            var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);

            /**
             * Podiamos ter passado os paramentro na funcao a cima, para mostrar para o cliente quais campos foram no relatorio
             * Mas vamos passar o DataSource qye e o terceiro paramentro e a fonte de dados
             * dataSource
             * new JRBeanCollectionDataSource() ->Temos varias Possibilidades de varias fontes de dados.
             * Mas como Ja temos uma consulta que nos retorna uma lista de venda diaria.
             * Essas vendas diarias sao objetos JAVA, podemos dizer que sao JAVA Beans
             * Por isso utilizamos o JRBeanCollectionDataSource
             * E um DataSource de colecao de Beans Java Dentro dele passamos uma colecction do Java Normal’
             */
            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);

            /**
             * Gerente de preenchimento do JasperRepoty
             * Temos que passar dois parametro
             * imputStream -> Fluxo de dados do nosso relatorio
             */
            var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
            /**
             * Vamos exportar o jasperPrint -> que e um Objeto que representa um relatorio Preenchido
             * Utilizando o JasperExportManager -> gerente de exportacao de relatorio Jasper
             * Vai nos retornar um Array de Bytes do PDF
             */

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new ReportException("Nao foi possivel Emitir Relatorio de vendas diarias", e);
        }
    }
}
