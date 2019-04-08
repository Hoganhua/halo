package run.halo.app.utils;

import run.halo.app.model.support.HaloConst;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Markdown utils
 *
 * @author : RYAN0UP
 * @date : 2018/11/14
 */
public class MarkdownUtils {

    /**
     * Front-matter extension
     */
    private static final Set<Extension> EXTENSIONS_YAML = Collections.singleton(YamlFrontMatterExtension.create());

    /**
     * Table extension
     */
    private static final Set<Extension> EXTENSIONS_TABLE = Collections.singleton(TablesExtension.create());

    /**
     * Parse Markdown content
     */
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * Render HTML content
     */
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS_YAML).extensions(EXTENSIONS_TABLE).build();

    /**
     * Render Markdown content
     *
     * @param content content
     * @return String
     * @see <a href="https://github.com/otale/tale/blob/master/src/main/java/com/tale/utils/TaleUtils.java">TaleUtils.java</a>
     */
    public static String renderMarkdown(String content) {
        final Node document = PARSER.parse(content);
        String renderContent = RENDERER.render(document);
        // render netease music short url
        if (content.contains(HaloConst.NETEASE_MUSIC_PREFIX)) {
            renderContent = content.replaceAll(HaloConst.NETEASE_MUSIC_REG_PATTERN, HaloConst.NETEASE_MUSIC_IFRAME);
        }
        // render bilibili video short url
        if (content.contains(HaloConst.BILIBILI_VIDEO_PREFIX)) {
            renderContent = content.replaceAll(HaloConst.BILIBILI_VIDEO_REG_PATTERN, HaloConst.BILIBILI_VIDEO_IFRAME);
        }
        // render youtube video short url
        if (content.contains(HaloConst.YOUTUBE_VIDEO_PREFIX)) {
            renderContent = content.replaceAll(HaloConst.YOUTUBE_VIDEO_REG_PATTERN, HaloConst.YOUTUBE_VIDEO_IFRAME);
        }
        return renderContent;
    }

    /**
     * Get front-matter
     *
     * @param content content
     * @return Map
     */
    public static Map<String, List<String>> getFrontMatter(String content) {
        final YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        final Node document = PARSER.parse(content);
        document.accept(visitor);
        return visitor.getData();
    }
}