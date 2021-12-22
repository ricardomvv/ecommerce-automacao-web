package homepage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test 
	public void testValidarCarrinhoZerado_ZeroItensNoCarrinho() { 
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		System.out.println(produtosNoCarrinho);
		assertThat(produtosNoCarrinho, is(0));
	}
	
	ProdutoPage produtoPage;
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() { 
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		
		String nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
		System.out.println(nomeProduto_ProdutoPage);
		System.out.println(precoProduto_ProdutoPage);
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
	}
	
	LoginPage loginPage;
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		//Clicar no botão 'Sign In' na Home Page
		loginPage = homePage.clicarBotaoSignIn();
		
		//Preencher usuario e senha
		loginPage.preencherEmail("ricardomvv@teste.com");
		loginPage.preencherPassword("teste");
		
		//Clicar no botão 'Sign In' para logar
		loginPage.clicarBotaoSignIn();
		
		//Validar se o usuário está logado de fato
		assertThat(homePage.estaLogado("Ricardo Martin"), is(true));
		
		carregarPaginaInicial();
		
	}
	
	@Test
	public void incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
		
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		
		//--Pré-condição
		//usuário logado
		if(!homePage.estaLogado("Ricardo Martin")) {
			testLoginComSucesso_UsuarioLogado();
		}
		
		//--Teste
		//Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		
		//Selecionar tamanho
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		
		System.out.println(listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		//Selecionar cor
		produtoPage.selecionarCorPreta();
		
		//Selecionar quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);
		
		//Adicionar no carrinho
		ModalProdutoPage modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		//Validacões
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
	
		System.out.println(modalProdutoPage.obterTamanhoProduto());
		System.out.println(modalProdutoPage.obterCorProduto());
		System.out.println(modalProdutoPage.obterQuantidadeProduto());
	}
}
