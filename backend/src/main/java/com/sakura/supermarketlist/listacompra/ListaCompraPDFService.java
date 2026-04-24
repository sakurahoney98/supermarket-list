package com.sakura.supermarketlist.listacompra;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sakura.supermarketlist.categoria.dto.CategoriaResponseDTO;
import com.sakura.supermarketlist.listacompra.dto.ListaCompraRequestDTO;

@Service
public class ListaCompraPDFService {

	public byte[] exportarListaEmPDF(List<ListaCompraRequestDTO> lista) {

		try {
			
			if (!lista.isEmpty()) {
				List<ListaCompraRequestDTO> listaFiltrada = lista.stream().filter(item -> item.quantidadeCompra() > 0)
						.toList();

				List<CategoriaResponseDTO> categorias = listaFiltrada.stream().map(ListaCompraRequestDTO::categoria)
						.distinct().toList();
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Document document = new Document(PageSize.A4);
				PdfWriter.getInstance(document, out);

				document.open();

				int colunas = categorias.size() > 1 ? 4 : 2;

				PdfPTable table = new PdfPTable(colunas);

				for (int index = 0; index < categorias.size(); index += 2) {

					CategoriaResponseDTO ideCategoria1 = categorias.get(index);

					List<ListaCompraRequestDTO> itensCategoria1 = listaFiltrada.stream()
							.filter(item -> item.categoria().id().equals(ideCategoria1.id())).toList();

					adicionarTituloDaCategoria(table, ideCategoria1.nome());

					List<ListaCompraRequestDTO> itensCategoria2 = new ArrayList<ListaCompraRequestDTO>();

					if (categorias.size() > index + 1) {
						CategoriaResponseDTO ideCategoria2 = categorias.get(index + 1);

						itensCategoria2 = listaFiltrada.stream()
								.filter(item -> item.categoria().id().equals(ideCategoria2.id())).toList();

						adicionarTituloDaCategoria(table, ideCategoria2.nome());

					}else if(colunas == 4) {
						adicionarLinhaEmBranco(table, 2);
					}

					for (int indexLista = 0; indexLista < itensCategoria1.size()
							|| indexLista < itensCategoria2.size(); indexLista++) {
						if (indexLista < itensCategoria1.size()) {
							adicionarItemCompra(table, itensCategoria1.get(indexLista).nome(),
									itensCategoria1.get(indexLista).quantidadeCompra(),
									itensCategoria1.get(indexLista).unidadeMedida());
						} else {
							adicionarLinhaEmBranco(table, 2);
						}

						if (indexLista < itensCategoria2.size()) {
							adicionarItemCompra(table, itensCategoria2.get(indexLista).nome(),
									itensCategoria2.get(indexLista).quantidadeCompra(),
									itensCategoria2.get(indexLista).unidadeMedida());
						} else {
							adicionarLinhaEmBranco(table, 2);
						}

					}

					adicionarLinhaEmBranco(table, colunas);

				}

				document.add(table);

				document.close();

				return out.toByteArray();

			}

		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return null;

	}

	private void adicionarTituloDaCategoria(PdfPTable table, String nomeCategoria) {
		PdfPCell tituloCategoria = new PdfPCell();
		Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
		tituloCategoria.addElement(new Phrase(nomeCategoria, font));
		tituloCategoria.setColspan(2);
		tituloCategoria.setBackgroundColor(new BaseColor(255, 190, 203));
		table.addCell(tituloCategoria);
	}

	private void adicionarItemCompra(PdfPTable table, String nome, Integer quantidade, String unidadeMedida) {

		table.addCell(nome + " (" + unidadeMedida + ")");
		table.addCell(String.valueOf(quantidade));
	}

	private void adicionarLinhaEmBranco(PdfPTable table, int quantidade) {

		for(int i = 0; i < quantidade; i++) {
			table.addCell(" ");
		}
		
		
	}

}
